package ru.nik.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.domain.Genre;
import ru.nik.library.security.AuthenticationSuccessHandlerImpl;
import ru.nik.library.service.BookService;

import java.util.*;
import ru.nik.library.service.UserService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private BookService service;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthenticationSuccessHandlerImpl successHandler;

	private List<Book> expected;

	@BeforeEach
	void setUp() {
		expected = new ArrayList<>();
		Book book1 = new Book("book 1", "description 1");
		Book book2 = new Book("book 2", "description 2");
		Set<Author> authors = new HashSet<>();
		authors.add(new Author("Пушкин"));
		authors.add(new Author("Кинг"));
		Set<Genre> genres = new HashSet<>();
		genres.add(new Genre("детектив"));
		genres.add(new Genre("роман"));
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("прикольно"));
		book1.setAuthors(authors);
		book1.setComments(comments);
		book1.setGenres(genres);
		book2.setGenres(genres);
		expected.add(book1);
		expected.add(book2);
	}

	@WithMockUser
	@Test
	void index() throws Exception {
		given(service.getAllBooks()).willReturn(expected);
		this.mvc.perform(get("/books")).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("welcome"))
			.andExpect(model().attribute("books", expected));
	}

	@WithMockUser(
		authorities = "ROLE_ADMIN"
	)
	@Test
	void edit() throws Exception {
		given(service.getBookById(0)).willReturn(expected.get(0));
		this.mvc.perform(get("/books/edit/{id}", "0")).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/edit"))
			.andExpect(model().attribute("authors", expected.get(0).getAuthorsNames()))
			.andExpect(model().attribute("genres", expected.get(0).getGenresNames()))
			.andExpect(model().attribute("book", expected.get(0)));
		verify(this.service, Mockito.atLeastOnce()).getBookById(0);
	}

	@WithMockUser
	@Test
	void view() throws Exception {
		given(service.getBookById(0)).willReturn(expected.get(0));
		this.mvc.perform(get("/books/view/{id}", "0")).andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("/viewbook"))
			.andExpect(model().attribute("authors", expected.get(0).getAuthorsNames()))
			.andExpect(model().attribute("genres", expected.get(0).getGenresNames()))
			.andExpect(model().attribute("book", expected.get(0)));
		verify(this.service, Mockito.atLeastOnce()).getBookById(0);
	}

	@WithMockUser(
		authorities = "ROLE_ADMIN"
	)
	@Test
	void delete() throws Exception {
		given(service.deleteBookById(1)).willReturn(true);
		this.mvc.perform(post("/books/delete").param("id", "1").with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/books"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/books"));
		verify(this.service, Mockito.atLeastOnce()).deleteBookById(1);
	}

	@WithMockUser(
		authorities = "ROLE_ADMIN"
	)
	@Test
	void addBook() throws Exception {
		expected.add(new Book("новая книжка", "ее описание"));
		given(service.addBook("новая книжка", "ее описание")).willReturn(true);
		this.mvc.perform(post("/books").param("name", "новая книжка")
			.param("description", "ее описание").with(csrf())).andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/books"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/books"));
		verify(this.service, Mockito.atLeastOnce()).addBook("новая книжка", "ее описание");
	}

	@WithMockUser(
		authorities = "ROLE_ADMIN"
	)
	@Test
	void updateBook() throws Exception {
		expected.set(1, new Book("обновленная кника", "ее описание"));
		given(service.updateBook(1, "обновленная кника", "ее описание")).willReturn(true);
		this.mvc.perform(post("/books/update").param("id", "1")
			.sessionAttr("name", "обновленная книжка").sessionAttr("description", "ее описание")
			.with(csrf()))
			.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/books"));
		verify(this.service, Mockito.atLeastOnce()).updateBook(1, "", "");
	}

	@WithMockUser(
		authorities = "ROLE_ADMIN"
	)
	@Test
	void updateBookAuthors() throws Exception {
		Book book1 = new Book("book 1", "description 1");
		Set<Author> authors = new HashSet<>();
		authors.add(new Author("Пушкин"));
		authors.add(new Author("Лермонтов"));
		expected.set(0, book1);

		given(service.updateBookAuthors(0, "Пушкин", "Лермонтов")).willReturn(true);
		this.mvc.perform(post("/books/update/authors").param("id", "0")
			.sessionAttr("authors", "Пушкин, Лермонтов").with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/books"));
	}

	@WithMockUser(
		authorities = "ROLE_ADMIN"
	)
	@Test
	void updateBookGenres() throws Exception {
		Book book1 = new Book("book 1", "description 1");
		Set<Genre> genres = new HashSet<>();
		genres.add(new Genre("учебник"));
		genres.add(new Genre("задачник"));
		expected.set(0, book1);

		given(service.updateBookGenres(0, "учебник", "задачник")).willReturn(true);
		this.mvc.perform(post("/books/update/genres").param("id", "0")
			.sessionAttr("genres", "учебник, задачник").with(csrf()))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/books"));
	}
}