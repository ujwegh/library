package ru.nik.library.controller.rest;


import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.dto.CommentDto;
import ru.nik.library.service.CommentService;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
	ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@AutoConfigureWebTestClient
class CommentFluxControllerTest {

	private WebTestClient webClient;

	@MockBean
	private CommentService service;

	private List<Comment> expected;

	@BeforeEach
	void setUp() {
		webClient = WebTestClient.bindToController(new CommentFluxController(service)).build();
		expected = new ArrayList<>();
		Comment one = new Comment("a", "какой-то коммент");
		Comment two = new Comment("b", "другой какой-то коммент");
		Book book = new Book("книга", "описание");
		book.setId("a");
		one.setBook(book);
		two.setBook(book);
		expected.add(one);
		expected.add(two);
	}


	@Test
	void getAllComments() throws Exception {
		Mockito.when(service.getAllComments("a")).thenReturn(Flux.just(expected.get(0),
			expected.get(1)));

		this.webClient.get()
			.uri("/rest/comments/{bookId}", "a")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.json("[{\"id\":\"a\",\"comment\":\"какой-то коммент\","
				+ "\"book\":{\"id\":\"a\",\"name\":\"книга\",\"description\":\"описание\",\"comments\":null,"
				+ "\"authors\":[],\"genres\":[],\"authorsNames\":\"\",\"genresNames\":\"\"}},"
				+ "{\"id\":\"b\",\"comment\":\"другой какой-то коммент\","
				+ "\"book\":{\"id\":\"a\",\"name\":\"книга\",\"description\":\"описание\",\"comments\":null,"
				+ "\"authors\":[],\"genres\":[],\"authorsNames\":\"\",\"genresNames\":\"\"}}]");

		verify(this.service, Mockito.atLeastOnce()).getAllComments("a");
	}

	@Test
	void deleteComment() throws Exception {
		Mockito.when(service.deleteCommentById("a","a")).thenReturn(Mono.empty());

		this.webClient.delete()
			.uri("/rest/comments/{bookId}/comment/{id}", "a", "a")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody().isEmpty();

		verify(this.service, Mockito.atLeastOnce()).deleteCommentById("a","a");
	}

	@Test
	void updateComment() throws Exception {
		Mono<Comment> com = Mono.just(expected.get(0)).map(comment -> {
			comment.setComment("новый, никому не нужный, коммент");
			return comment;
		});

		Mockito.when(service.updateBookComment("a", "a", "новый, никому не нужный, коммент"))
			.thenReturn(com);

		this.webClient.put()
			.uri("/rest/comments/{bookId}", "a")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(new CommentDto("a", "a","новый, никому не нужный, коммент")), CommentDto.class)
			.exchange().expectStatus().isOk()
			.expectBody()
			.json(asJsonString(new CommentDto("a","a", "новый, никому не нужный, коммент")))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.comment").isEqualTo("новый, никому не нужный, коммент");

		verify(this.service, Mockito.atLeastOnce()).updateBookComment("a", "a", "новый, никому не нужный, коммент");
	}

	@Test
	void addComment() throws Exception {

		Mono<Comment> com = Mono.just(expected.get(0)).map(comment -> {
			comment.setId("c");
			comment.setComment("новый, никому не нужный, коммент");
			return comment;
		});

		Mockito.when(service.addComment("a", "новый, никому не нужный, коммент"))
			.thenReturn(com);
		expected.add(new Comment("c","новый, никому не нужный, коммент"));

		this.webClient.post()
			.uri("/rest/comments/{bookId}", "a")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body(Mono.just(new CommentDto("c", "a","новый, никому не нужный, коммент")), CommentDto.class)
			.exchange().expectStatus().isOk()
			.expectBody()
			.json(asJsonString(new CommentDto("c", "a","новый, никому не нужный, коммент")))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.comment").isEqualTo("новый, никому не нужный, коммент");

		verify(this.service, Mockito.atLeastOnce()).addComment("a", "новый, никому не нужный, коммент");
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}