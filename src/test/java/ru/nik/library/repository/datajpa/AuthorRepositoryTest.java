package ru.nik.library.repository.datajpa;

import com.mongodb.MongoClient;
import java.time.Duration;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.nik.library.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = AuthorRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
class AuthorRepositoryTest {

	@Autowired
	private AuthorRepository repository;

	private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
		"testdb");

	@BeforeEach
	public void init() {
		Author one = new Author("abc", "Пушкин");
		Author two = new Author("aaa", "Кинг");
		Author three = new Author("www", "Просто автор");
		mongoTemplate.dropCollection("authors");
		mongoTemplate.save(one);
		mongoTemplate.save(two);
		mongoTemplate.save(three);
	}

	@Test
	void findAll() {
		List<Author> authors = new ArrayList<>();
		Author one = new Author("abc", "Пушкин");
		Author two = new Author("aaa", "Кинг");
		Author three = new Author("www", "Просто автор");
		authors.add(one);
		authors.add(two);
		authors.add(three);

		repository.findAll()
			.collectList()
			.as(StepVerifier::create)
			.assertNext(authors1 -> {
				assertNotNull(authors);
				assertEquals(3, authors.size());
			})
			.verifyComplete();
	}

	@Test
	void findAllByNameIn() {
		List<Author> authors = new ArrayList<>();
		Author one = new Author("abc", "Пушкин");
		Author two = new Author("aaa", "Кинг");
		authors.add(one);
		authors.add(two);

		repository.findAllByNameIn(one.getName(), two.getName())
			.collectList()
			.as(StepVerifier::create)
			.assertNext(authors1 -> {
				assertNotNull(authors);
				assertEquals(authors.size(), 2);
			})
			.verifyComplete();
	}

	@Test
	void save() {
		Author expected = new Author("Новый автор");

		repository.save(expected)
			.as(StepVerifier::create)
			.assertNext(author -> {
				assertNotNull(author.getId());
				assertEquals(expected.getName(), author.getName());
			})
			.verifyComplete();
	}

	@Test
	void update() {
        Mono<Author> authorMono = repository.findById("abc").doOnSuccess(author -> author.setName("Сказочный"));

        Mono<Author> updated = repository.save(authorMono.block());
        StepVerifier
			.create(updated)
			.assertNext(author -> {
				assertNotNull(author.getId());
				assertEquals("Сказочный", author.getName());
			}).verifyComplete();
	}

	@Test
	void findById() {
		Author expected = new Author("abc", "Пушкин");

		repository.findById(expected.getId())
			.as(StepVerifier::create)
			.assertNext(author -> {
				assertNotNull(author);
				assertEquals(expected.toString(), author.toString());
			}).verifyComplete();
	}

	@Test
	void findByName() {
		Author expected = new Author("Пушкин");

		repository.findByName(expected.getName())
			.as(StepVerifier::create)
			.assertNext(author -> {
				assertNotNull(author);
				assertEquals(expected.getName(), author.getName());
			}).verifyComplete();
	}


	@Test
	void deleteById() {
		repository.deleteById("abc")
			.as(StepVerifier::create)
			.verifyComplete();

		assertNull(repository.findById("abc").block());
	}
}