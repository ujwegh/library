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
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
	ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableMongoRepositories(basePackages = {"ru.nik.library.repository"})
@EnableAutoConfiguration
@PropertySource("classpath:application-test.properties")
class BookServiceImplTest {

	@Autowired
	private BookService service;

	private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
		"testdb");

	@BeforeEach
	public void init() {
		Book one = new Book("книга", "интересная");
		Book two = new Book("журнал", "новый");
		mongoTemplate.dropCollection("books");
		mongoTemplate.save(one);
		mongoTemplate.save(two);
	}

	@Test
	void addBookTest() {
		Book expected = new Book("математика", "для школы");
		service.addBook(expected.getName(), expected.getDescription())
			.as(StepVerifier::create)
			.assertNext(book -> {
				assertNotNull(book);
				assertEquals(expected.getName(), book.getName());
				assertEquals(expected.getDescription(), book.getDescription());
			}).verifyComplete();
	}

	@Test
	void deleteBookByIdTest() {
		List<Book> allBooks = service.getAllBooks().collectList().block();
		Book expected = allBooks.get(0);
		service.deleteBookById(expected.getId())
			.as(StepVerifier::create)
			.verifyComplete();

		assertNull(service.getBookById(expected.getId()).block());
	}

	@Test
	void updateBookTest() {
		List<Book> allBooks = service.getAllBooks().collectList().block();
		Book expected = allBooks.get(0);
		expected.setName("новый автор");
		expected.setDescription("хорошая книжка");
		service.updateBook(expected.getId(), expected.getName(), expected.getDescription())
			.as(StepVerifier::create)
			.assertNext(book -> {
				assertNotNull(book);
				assertEquals(expected.toString(), book.toString());
			}).verifyComplete();
	}

	@Test
	void getBookByIdTest() {
		List<Book> allBooks = service.getAllBooks().collectList().block();
		Book expected = allBooks.get(0);
		service.getBookById(expected.getId())
			.as(StepVerifier::create)
			.assertNext(book -> {
				assertNotNull(book);
				assertEquals(expected.toString(), book.toString());
			}).verifyComplete();
	}

	@Test
	void getAllBooksTest() {
		Book one = new Book("книга 1", "описание");
		Book two = new Book("книга 2", "описание");
		List<Book> expected = new ArrayList<>();
		expected.add(one);
		expected.add(two);
		service.getAllBooks().collectList()
			.as(StepVerifier::create)
			.assertNext(books -> {
				assertNotNull(books);
				assertEquals(expected.size(), books.size());
			}).verifyComplete();
	}

	@Test
	void updateBookAuthors() {
		List<Book> allBooks = service.getAllBooks().collectList().block();
		Book expected = allBooks.get(0);
		Author one = new Author("Петя");
		Author two = new Author("Кинг");

		Set<Author> authors = new HashSet<>(Set.of(one, two));
		expected.setAuthors(authors);
		service.updateBookAuthors(expected.getId(), one.getName(), two.getName())
			.as(StepVerifier::create)
			.assertNext(book -> {
				assertNotNull(book);
				assertEquals(expected.toString(), book.toString());
			}).verifyComplete();
	}

	@Test
	void updateBookGenres() {
		List<Book> allBooks = service.getAllBooks().collectList().block();
		Book expected = allBooks.get(0);
		Genre one = new Genre("жанр 1");
		Genre two = new Genre("жанр 2");

		Set<Genre> genres = new HashSet<>(Set.of(one, two));
		expected.setGenres(genres);
		service.updateBookGenres(expected.getId(), one.getName(), two.getName())
			.as(StepVerifier::create)
			.assertNext(book -> {
				assertNotNull(book);
				assertEquals(expected.toString(), book.toString());
			}).verifyComplete();
	}
}