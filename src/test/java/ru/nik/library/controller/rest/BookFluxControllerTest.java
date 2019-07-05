package ru.nik.library.controller.rest;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.domain.Genre;
import ru.nik.library.dto.BookDto;
import ru.nik.library.service.BookService;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
	ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@AutoConfigureWebTestClient
class BookFluxControllerTest {

	private WebTestClient webClient;

	@MockBean
	private BookService service;

	private List<Book> expected;

	@BeforeEach
	void setUp() {
		webClient = WebTestClient.bindToController(new BookFluxController(service)).build();
		expected = new ArrayList<>();
		Book book1 = new Book("a", "book1", "description1");
		Book book2 = new Book("b", "book2", "description2");
		Set<Author> authors = new HashSet<>();
		authors.add(new Author("a", "Пушкин"));
		authors.add(new Author("b", "Кинг"));
		Set<Genre> genres = new HashSet<>();
		genres.add(new Genre("a", "детектив"));
		genres.add(new Genre("b", "роман"));
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("a", "прикольно"));
		book1.setAuthors(authors);
		book1.setComments(comments);
		book1.setGenres(genres);
		book2.setGenres(genres);
		expected.add(book1);
		expected.add(book2);
	}


	@Test
	void getAllBooks() throws Exception {
		Mockito.when(service.getAllBooks()).thenReturn(Flux.just(expected.get(0), expected.get(1)));

		this.webClient.get()
			.uri("/rest/books")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.json(
				"[{\"id\":\"a\",\"name\":\"book1\",\"description\":\"description1\",\"commentCount\":1,"
					+ "\"authorNames\":[\"Пушкин\",\"Кинг\"],\"genreNames\":[\"роман\",\"детектив\"]},"
					+ "{\"id\":\"b\",\"name\":\"book2\",\"description\":\"description2\",\"commentCount\":0,"
					+ "\"authorNames\":[],\"genreNames\":[\"роман\",\"детектив\"]}]");

		verify(this.service, Mockito.atLeastOnce()).getAllBooks();
	}

	@Test
	void deleteBook() throws Exception {
		Mockito.when(service.deleteBookById("a")).thenReturn(Mono.empty());

		this.webClient.delete()
			.uri("/rest/books/{id}", "a")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody().isEmpty();

		verify(this.service, Mockito.atLeastOnce()).deleteBookById("a");
	}

	@Test
	void updateBook() throws Exception {

		Mono<Book> bookMono = Mono.just(expected.get(0)).map(book -> {
			book.setName("Новая книга");
			book.setDescription("Новое описание");
			return book;
		});
		Mockito.when(service.updateBook("a", "Новая книга", "Новое описание"))
			.thenReturn(Mono.just(new Book("a", "Новая книга", "Новое описание")));

		this.webClient.put()
			.uri("/rest/books/{id}", "a")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(
				new BookDto("a", "Новая книга", "Новое описание", 0, Collections.emptyList(),
					Collections.emptyList())), BookDto.class)
			.exchange().expectStatus().isOk()
			.expectBody()
			.json(asJsonString(
				new BookDto("a", "Новая книга", "Новое описание", 0, Collections.emptyList(),
					Collections.emptyList())))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.name").isEqualTo("Новая книга")
			.jsonPath("$.description").isEqualTo("Новое описание");

		verify(this.service, Mockito.atLeastOnce())
			.updateBook("a", "Новая книга", "Новое описание");
	}

	@Test
	void addBook() throws Exception {
		Mockito.when(service.addBook("учебник", "физика"))
			.thenReturn(Mono.just(new Book("c", "учебник", "физика")));
		expected.add(new Book("учебник", "физика"));

		this.webClient.post()
			.uri("/rest/books")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(
				new BookDto("c", "учебник", "физика", 0, Collections.emptyList(),
					Collections.emptyList())), BookDto.class)
			.exchange().expectStatus().isOk()
			.expectBody()
			.json(asJsonString(
				new BookDto("c", "учебник", "физика", 0, Collections.emptyList(),
					Collections.emptyList())))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.name").isEqualTo("учебник")
			.jsonPath("$.description").isEqualTo("физика");

		verify(this.service, Mockito.atLeastOnce()).addBook("учебник", "физика");
	}

	@Test
	void updateBookAuthors() throws Exception {
		Mockito.when(service.updateBookAuthors("a", "Лермонтов"))
			.thenReturn(Mono.just(expected.get(0)).map(book -> {
				Set<Author> authors = new HashSet<>();
				authors.add(new Author("Лермонтов"));
				book.setAuthors(authors);
				return book;
			}));

		this.webClient.post()
			.uri("/rest/books/{id}/authors", "a")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.syncBody("Лермонтов")
			.exchange().expectStatus().isOk()
			.expectBody()
			.json(asJsonString(
				new BookDto("a", "book1", "description1", 1, Arrays.asList("Пушкин"),
					Arrays.asList("детектив", "роман"))))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.name").isEqualTo("book1")
			.jsonPath("$.description").isEqualTo("description1")
			.jsonPath("$.authorNames").isEqualTo("Лермонтов");

		verify(this.service, Mockito.atLeastOnce()).updateBookAuthors("a", "Лермонтов");
	}

	@Test
	void updateBookGenres() throws Exception {
		Mockito.when(service.updateBookGenres("a", "журнал"))
			.thenReturn(Mono.just(expected.get(0)).map(book -> {
			Set<Genre> genres = new HashSet<>();
			genres.add(new Genre("журнал"));
			book.setGenres(genres);
			return book;
		}));

		this.webClient.post()
			.uri("/rest/books/{id}/genres", "a")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.syncBody("журнал")
			.exchange().expectStatus().isOk()
			.expectBody()
			.json(asJsonString(
				new BookDto("a", "book1", "description1", 1, Arrays.asList("Пушкин", "Кинг"),
					Arrays.asList("журнал"))))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.name").isEqualTo("book1")
			.jsonPath("$.description").isEqualTo("description1")
			.jsonPath("$.genreNames").isEqualTo("журнал");

		verify(this.service, Mockito.atLeastOnce()).updateBookGenres("a", "журнал");
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}