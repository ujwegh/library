package ru.nik.library.controller.rest;


import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Author;
import ru.nik.library.dto.AuthorDto;
import ru.nik.library.service.AuthorService;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
	InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
	ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@AutoConfigureWebTestClient
class AuthorFluxControllerTest {

	private WebTestClient webClient;

	@MockBean
	private AuthorService service;

	private List<Author> expected;

	@BeforeEach
	void setUp() {
		webClient = WebTestClient.bindToController(new AuthorFluxController(service)).build();
		expected = new ArrayList<>();
		expected.add(new Author("a", "Пушкин"));
		expected.add(new Author("b", "Кинг"));
	}

	@Test
	void getAuthors() throws Exception {
		Mockito.when(this.service.getAllAuthors())
			.thenReturn(Flux.just(new Author("a", "Пушкин"), new Author("b", "Кинг")));

		this.webClient.get()
			.uri("/rest/authors")
			.accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus()
			.isOk()
			.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.json("[{\"id\":\"a\",\"name\":\"Пушкин\",\"bookNames\":[]},"
					+ "{\"id\":\"b\",\"name\":\"Кинг\",\"bookNames\":[]}]");

		verify(this.service, Mockito.atLeastOnce()).getAllAuthors();
	}

	@Test
	void deleteAuthor() throws Exception {
		Mockito.when(service.deleteAuthorById("a")).thenReturn(Mono.empty());

		this.webClient.delete()
			.uri("/rest/authors/{id}", "a")
			.exchange()
			.expectStatus()
			.isOk()
			.expectBody().isEmpty();

		verify(this.service, Mockito.atLeastOnce()).deleteAuthorById("a");
	}

	@Test
	void updateAuthor() throws Exception {
		Mockito.when(service.updateAuthor("a", "Лермонтов"))
			.thenReturn(Mono.just(new Author("a", "Лермонтов")));

		this.webClient.put()
			.uri("/rest/authors/{id}", "a")
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.body(Mono.just(new AuthorDto("a", "Лермонтов", Collections.emptyList())),
				AuthorDto.class)
			.exchange().expectStatus().isOk()
			.expectBody()
			.json(asJsonString(new AuthorDto("a", "Лермонтов", Collections.emptyList())))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.name").isEqualTo("Лермонтов");

		verify(this.service, Mockito.atLeastOnce()).updateAuthor("a", "Лермонтов");
	}

    @Test
    void addAuthor() throws Exception {
        Mockito.when(service.addAuthor("Лермонтов")).thenReturn(Mono.just(new Author("c", "Лермонтов")));
        expected.add(new Author("Лермонтов"));

		this.webClient.post()
			.uri("/rest/authors")
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(new AuthorDto(null, "Лермонтов", Collections.emptyList())),
				AuthorDto.class)
			.exchange().expectStatus().isOk()
			.expectBody().json(asJsonString(new AuthorDto("c", "Лермонтов", Collections.emptyList())))
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.name").isEqualTo("Лермонтов");

        verify(this.service, Mockito.atLeastOnce()).addAuthor( "Лермонтов");
    }

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}