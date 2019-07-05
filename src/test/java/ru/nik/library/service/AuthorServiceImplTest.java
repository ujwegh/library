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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.nik.library.domain.Author;

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
@TestPropertySource("classpath:application-test.properties")
class AuthorServiceImplTest {

	@Autowired
	private AuthorService service;

	private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
		"testdb");

	@BeforeEach
	void init() {
		Author one = new Author("Достоевский");
		Author two = new Author("Лермонтов");
		mongoTemplate.dropCollection("authors");
		mongoTemplate.save(one);
		mongoTemplate.save(two);
	}

	@Test
	void addAuthorTest() {
		Author expected = new Author("Толкин");
		service.addAuthor(expected.getName())
			.as(StepVerifier::create)
			.assertNext(author -> {
				assertNotNull(author);
				assertEquals(expected.getName(), author.getName());
			}).verifyComplete();
	}

	@Test
	void deleteAuthorByIdTest() {
		Mono<Author> author = service.getAuthorByName("Достоевский");
		String id = author.block().getId();
		service.deleteAuthorById(author.block().getId())
			.as(StepVerifier::create)
			.verifyComplete();

		assertNull(service.getAuthorById(id).block());
	}

	@Test
	void updateAuthorTest() {
		Mono<Author> expected = service.getAuthorByName("Достоевский");
		service.updateAuthor(expected.block().getId(), "Новый автор")
			.as(StepVerifier::create)
			.assertNext(author -> {
				assertNotNull(author);
				assertEquals("Новый автор", author.getName());
			}).verifyComplete();
	}

	@Test
	void getAuthorByNameTest() {
		Author expected = new Author("Лермонтов");
		service.getAuthorByName("Лермонтов")
			.as(StepVerifier::create)
			.assertNext(author -> {
				assertNotNull(author);
				assertEquals(expected.getName(), author.getName());
			}).verifyComplete();
	}

	@Test
	void getAuthorByIdTest() {
		Mono<Author> expected = service.getAuthorByName("Достоевский");
		String id = expected.block().getId();
		String ex = expected.block().toString();
		service.getAuthorById(id)
			.as(StepVerifier::create)
			.assertNext(author -> {
				assertNotNull(author);
				assertEquals(ex, author.toString());
			}).verifyComplete();
	}

	@Test
	void getAllTest() {
		service.getAllAuthors().collectList()
			.as(StepVerifier::create)
			.assertNext(authors -> {
				assertNotNull(authors);
				assertEquals(2, authors.size());
			}).verifyComplete();
	}
}