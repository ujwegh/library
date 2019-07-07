package ru.nik.library.controller.rest;


import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.security.AuthenticationSuccessHandlerImpl;
import ru.nik.library.service.CommentService;
import ru.nik.library.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentRestController.class)
class CommentRestControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CommentService service;

	@MockBean
	private UserService userService;

	@MockBean
	private AuthenticationSuccessHandlerImpl successHandler;

	private List<Comment> expected;

	@BeforeEach
	void setUp() {
		expected = new ArrayList<>();
		Comment one = new Comment(0, "какой-то коммент");
		Comment two = new Comment(1, "другой какой-то коммент");
		Book book = new Book("книга", "описание");
		book.setId(0);
		one.setBook(book);
		two.setBook(book);
		expected.add(one);
		expected.add(two);
	}

	@WithMockUser
	@Test
	void getAllComments() throws Exception {
		Mockito.when(service.getAllComments(0)).thenReturn(expected);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/comments/{bookId}", 0)
			.accept(MediaType.APPLICATION_JSON_VALUE);
		mvc.perform(requestBuilder).andExpect(status().isOk()).andExpect(content().json("[{\"id\":0,\"comment\":\"какой-то коммент\",\"book\":{\"id\":0,\"name\":\"книга\","
			+ "\"description\":\"описание\",\"comments\":null,\"authors\":[],\"genres\":[],\"new\":false,"
			+ "\"genresNames\":\"\",\"authorsNames\":\"\"},\"new\":false},{\"id\":1,\"comment\":\"другой какой-то коммент\","
			+ "\"book\":{\"id\":0,\"name\":\"книга\",\"description\":\"описание\",\"comments\":null,\"authors\":[],"
			+ "\"genres\":[],\"new\":false,\"genresNames\":\"\",\"authorsNames\":\"\"},\"new\":false}]")).andReturn();

		verify(this.service, Mockito.atLeastOnce()).getAllComments(0);
	}

	@WithMockUser
	@Test
	void deleteComment() throws Exception {
		Mockito.when(service.deleteCommentById(0,0)).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/comments/{bookId}/comment/{id}", 0, 0)
			.with(csrf())
			.accept(MediaType.APPLICATION_JSON_VALUE);
		this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
		verify(this.service, Mockito.atLeastOnce()).deleteCommentById(0,0);
	}

	@WithMockUser
	@Test
	void updateComment() throws Exception {
		Mockito.when(service.updateBookComment(0, 0, "новый, никому не нужный, коммент")).thenReturn(true);
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/comments/{bookId}", 0)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(asJsonString(new Comment(0, "новый, никому не нужный, коммент")));
		this.mvc.perform(requestBuilder).andExpect(status().isOk())
			.andExpect(content().json("{\"id\":0,\"comment\":\"новый, никому не нужный, коммент\","
				+ "\"book\":null,\"new\":false}")).andReturn();
		verify(this.service, Mockito.atLeastOnce()).updateBookComment(0, 0, "новый, никому не нужный, коммент");
	}

	@WithMockUser
	@Test
	void addComment() throws Exception {
		Mockito.when(service.addComment(0, "новый, никому не нужный, коммент")).thenReturn(true);
		expected.add(new Comment("новый, никому не нужный, коммент"));
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/comments/{bookId}",0)
			.with(csrf())
			.contentType(MediaType.APPLICATION_JSON)
			.content(asJsonString(new Comment("новый, никому не нужный, коммент")));
		this.mvc.perform(requestBuilder)
			.andExpect(status().isOk()).andExpect(content().json("{\"id\":null,\"comment\":\"новый, "
				+ "никому не нужный, коммент\",\"book\":null,\"new\":true}")).andReturn();
		verify(this.service, Mockito.atLeastOnce()).addComment(0, "новый, никому не нужный, коммент");
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}