package ru.nik.library.repository.datajpa;


import com.mongodb.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = {CommentRepository.class, BookRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
class CommentRepositoryTest {

	@Autowired
	private CommentRepository repository;
	@Autowired
	private BookRepository bookRepository;

	private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
		"testdb");

	@BeforeEach
	public void init() {
		Book book = new Book("aaa", "книга", "интересная");
		Book book2 = new Book("bbb", "книга 2", "можно почитать");
		mongoTemplate.dropCollection("books");
		mongoTemplate.save(book);
		mongoTemplate.save(book2);
		Comment one = new Comment("aa", "коммент 1");
		one.setBook(book);
		Comment two = new Comment("bb", "коммент 2");
		two.setBook(book);
		Comment three = new Comment("cc", "коммент 1");
		three.setBook(book2);
		mongoTemplate.dropCollection("comments");
		mongoTemplate.save(one);
		mongoTemplate.save(two);
		mongoTemplate.save(three);
	}

	@Test
	void save() {
		Mono<Book> book = bookRepository.findById("aaa");

		Comment comment = new Comment("dd", "какой-то коммент");
		comment.setBook(book.block());

		repository.save(comment)
			.as(StepVerifier::create)
			.assertNext(comment1 -> {
				assertNotNull(comment1.getId());
				assertEquals(comment1.toString(), comment.toString());
			}).verifyComplete();
	}


	@Test
	void findAllByBook_Id() {
		repository.findAllByBook_Id("aaa")
			.collectList()
			.as(StepVerifier::create)
			.assertNext(comments -> {
				assertNotNull(comments);
				assertEquals(2, comments.size());
			}).verifyComplete();
	}

	@Test
	void findByIdAndBook_Id() {
		Comment comment = new Comment("bb", "коммент 2");
		repository.findByIdAndBook_Id("bb", "aaa")
			.as(StepVerifier::create)
			.assertNext(comment1 -> {
				assertNotNull(comment1);
				assertEquals(comment.getId(), comment1.getId());
				assertEquals(comment.getComment(), comment1.getComment());
			}).verifyComplete();
	}

	@Test
	void deleteByIdAndBook_Id() {
		repository.deleteByIdAndBook_Id("bb", "aaa")
			.as(StepVerifier::create)
			.expectNext(1L)
			.verifyComplete();

		Flux<Comment> comments = repository.findAllByBook_Id("aaa");
		assertNotNull(comments);
		assertEquals(1, comments.collectList().block().size());
		assertNull(repository.findByIdAndBook_Id("bb", "aaa").block());
	}
}