package ru.nik.library.controller.rest;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.domain.Genre;
import ru.nik.library.dto.BookDto;
import ru.nik.library.service.BookService;

@RunWith(SpringRunner.class)
@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BookService service;

	private List<Book> expected;

	@BeforeEach
	void setUp() {
		expected = new ArrayList<>();
		Book book1 = new Book(1, "book1", "description1");
		Book book2 = new Book(2, "book2", "description2");
		Set<Author> authors = new HashSet<>();
		authors.add(new Author(1, "Пушкин"));
		authors.add(new Author(2, "Кинг"));
		Set<Genre> genres = new HashSet<>();
		genres.add(new Genre(1, "детектив"));
		genres.add(new Genre(2, "роман"));
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment(1, "прикольно"));
		book1.setAuthors(authors);
		book1.setComments(comments);
		book1.setGenres(genres);
		book2.setGenres(genres);
		expected.add(book1);
		expected.add(book2);
	}


	@Test
	void getAllBooks() throws Exception {
		Mockito.when(service.getAllBooks()).thenReturn(expected);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/books")
			.accept(MediaType.APPLICATION_JSON_VALUE);
		MvcResult result = mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();

		String expectedString = "[{\"id\":1,\"name\":\"book1\",\"description\":\"description1\","
			+ "\"commentCount\":1,\"authorNames\":[\"Пушкин\",\"Кинг\"],\"genreNames\":[\"роман\","
			+ "\"детектив\"]},{\"id\":2,\"name\":\"book2\",\"description\":\"description2\",\"commentCount\":0,"
			+ "\"authorNames\":[],\"genreNames\":[\"роман\",\"детектив\"]}]";

		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
		verify(this.service, Mockito.atLeastOnce()).getAllBooks();
	}

	@Test
	void deleteBook() throws Exception {
		Mockito.when(service.deleteBookById(1)).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/books/{id}", 1)
			.accept(MediaType.APPLICATION_JSON_VALUE);
		this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		verify(this.service, Mockito.atLeastOnce()).deleteBookById(1);
	}

	@Test
	void updateBook() throws Exception {
		Mockito.when(service.updateBook(1, "Новая книга", "Новое описание")).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/books/{id}", 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content(asJsonString(new BookDto(1, "Новая книга", "Новое описание",
				0, Collections.emptyList(), Collections.emptyList())));
		MvcResult result = this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		String expectedString = "{\"id\":1,\"name\":\"Новая книга\",\"description\":\"Новое описание\",\"commentCount\":0,\"authorNames\":[],\"genreNames\":[]}";
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
		verify(this.service, Mockito.atLeastOnce()).updateBook(1, "Новая книга", "Новое описание");
	}

	@Test
	void addBook() throws Exception {
		Mockito.when(service.addBook("учебник", "физика")).thenReturn(true);
		expected.add(new Book("учебник", "физика"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/books")
			.contentType(MediaType.APPLICATION_JSON)
			.content(asJsonString(new BookDto("учебник", "физика")));
		MvcResult result = this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		String expectedString = "{\"id\":null,\"name\":\"учебник\",\"description\":\"физика\",\"commentCount\":null,\"authorNames\":null,\"genreNames\":null}";
		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
		verify(this.service, Mockito.atLeastOnce()).addBook("учебник", "физика");
	}

	@Test
	void updateBookAuthors() throws Exception {
		Mockito.when(service.updateBookAuthors(1, "Лермонтов", "Достоевский")).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/books/{id}/authors", 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content("Лермонтов, Достоевский");
		this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		verify(this.service, Mockito.atLeastOnce()).updateBookAuthors(1, "Лермонтов", "Достоевский");
	}

	@Test
	void updateBookGenres() throws Exception {
		Mockito.when(service.updateBookGenres(1, "учебник", "задачник")).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/books/{id}/genres", 1)
			.contentType(MediaType.APPLICATION_JSON)
			.content("учебник, задачник");
		this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		verify(this.service, Mockito.atLeastOnce()).updateBookGenres(1, "учебник", "задачник");
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}