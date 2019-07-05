package ru.nik.library.service;

import com.mongodb.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
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
import ru.nik.library.domain.Genre;

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
class GenreServiceImplTest {

	@Autowired
	private GenreService service;

	private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
		"testdb");

	@BeforeEach
	public void init() {
		Genre one = new Genre("сказки");
		Genre two = new Genre("фантастика");
		mongoTemplate.dropCollection("genres");
		mongoTemplate.save(one);
		mongoTemplate.save(two);
	}

	@Test
	void addGenreTest() {
		Genre expected = new Genre("детектив");
		service.addGenre(expected.getName())
			.as(StepVerifier::create)
			.assertNext(genre -> {
				assertNotNull(genre);
				assertEquals(expected.getName(), genre.getName());
			}).verifyComplete();
	}

	@Test
	void deleteGenreByIdTest() {
		Mono<Genre> genre = service.getGenreByName("сказки");
		String id = genre.block().getId();
		service.deleteGenreById(genre.block().getId())
			.as(StepVerifier::create)
			.verifyComplete();

		assertNull(service.getGenreById(id).block());
	}

	@Test
	void updateGenreTest() {
		Mono<Genre> expected = service.getGenreByName("сказки");
		service.updateGenre(expected.block().getId(), "Новый жанр")
			.as(StepVerifier::create)
			.assertNext(genre -> {
				assertNotNull(genre);
				assertEquals("Новый жанр", genre.getName());
			}).verifyComplete();
	}

	@Test
	void getGenreByNameTest() {
		Genre expected = new Genre("сказки");
		service.getGenreByName("сказки")
			.as(StepVerifier::create)
			.assertNext(genre -> {
				assertNotNull(genre);
				assertEquals(expected.getName(), genre.getName());
			}).verifyComplete();
	}

	@Test
	void getGenreByIdTest() {
		Mono<Genre> expected = service.getGenreByName("сказки");
		String id = expected.block().getId();
		String ex = expected.block().toString();
		service.getGenreById(id)
			.as(StepVerifier::create)
			.assertNext(genre -> {
				assertNotNull(genre);
				assertEquals(ex, genre.toString());
			}).verifyComplete();
	}

	@Test
	void getAllGenresTest() {
		service.getAllGenres().collectList()
			.as(StepVerifier::create)
			.assertNext(genres -> {
				assertNotNull(genres);
				assertEquals(2, genres.size());
			}).verifyComplete();
	}
}