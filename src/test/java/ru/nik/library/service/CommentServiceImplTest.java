package ru.nik.library.service;

import com.mongodb.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
	ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableMongoRepositories(basePackages = {"ru.nik.library.repository"})
@EnableAutoConfiguration
@PropertySource("classpath:application-test.properties")
class CommentServiceImplTest {

	@Autowired
	private CommentService service;

	@Autowired
	private BookService bookService;

	private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
		"testdb");

	@BeforeEach
	public void init() {
		Book book = new Book("книга", "интересная");
		Book book2 = new Book("книга 2", "можно почитать");
		mongoTemplate.dropCollection("books");
		mongoTemplate.save(book);
		mongoTemplate.save(book2);
		Comment one = new Comment("коммент 1");
		one.setBook(book);
		Comment two = new Comment("коммент 2");
		two.setBook(book);
		Comment three = new Comment("коммент 1");
		three.setBook(book2);
		mongoTemplate.dropCollection("comments");
		mongoTemplate.save(one);
		mongoTemplate.save(two);
		mongoTemplate.save(three);
	}

	@Test
	void addComment() {
		Comment comment = new Comment("новый коментарий");
		List<Book> allBooks = bookService.getAllBooks().collectList().block();
		service.addComment(allBooks.get(0).getId(), comment.getComment())
			.as(StepVerifier::create)
			.assertNext(comment1 -> {
				assertNotNull(comment1);
				assertEquals(comment.getComment(), comment1.getComment());
			}).verifyComplete();
	}

	@Test
	void deleteCommentById() {
		List<Book> allBooks = bookService.getAllBooks().collectList().block();
		List<Comment> allComments = service.getAllComments(allBooks.get(0).getId()).collectList()
			.block();
		String bookId = allBooks.get(0).getId();
		String commentId = allComments.get(0).getId();

		service.deleteCommentById(commentId, bookId)
			.as(StepVerifier::create)
			.verifyComplete();

		List<Comment> comments = service.getAllComments(bookId).collectList().block();
		assertEquals(1, comments.size());
		assertNull(service.getCommentById(commentId, bookId).block());
	}

	@Test
	void updateBookComment() {
		List<Book> allBooks = bookService.getAllBooks().collectList().block();
		List<Comment> allComments = service.getAllComments(allBooks.get(0).getId()).collectList()
			.block();
		String bookId = allBooks.get(0).getId();
		String commentId = allComments.get(0).getId();

		Comment comment = service.getCommentById(commentId, bookId).block();
		comment.setComment("измененный комент");

		service.updateBookComment(commentId, bookId, comment.getComment())
			.as(StepVerifier::create)
			.assertNext(comment1 -> {
				assertNotNull(comment1);
				assertEquals(comment.toString(), comment1.toString());
			}).verifyComplete();
	}

	@Test
	void getCommentById() {
		List<Book> allBooks = bookService.getAllBooks().collectList().block();
		List<Comment> allComments = service.getAllComments(allBooks.get(0).getId()).collectList()
			.block();
		String bookId = allBooks.get(0).getId();
		String commentId = allComments.get(0).getId();

		Comment comment = new Comment(commentId, "коммент 1");
		service.getCommentById(commentId, bookId)
			.as(StepVerifier::create)
			.assertNext(comment1 -> {
				assertNotNull(comment1);
				assertEquals(comment.getComment(), comment1.getComment());
			}).verifyComplete();
	}

	@Test
	void getAllComments() {
		List<Book> allBooks = bookService.getAllBooks().collectList().block();
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("интересная книга"));
		comments.add(new Comment("не игтересная книга"));

		service.getAllComments(allBooks.get(0).getId()).collectList()
			.as(StepVerifier::create)
			.assertNext(comments1 -> {
				assertNotNull(comments1);
				assertEquals(comments.size(), comments1.size());
			}).verifyComplete();
	}
}