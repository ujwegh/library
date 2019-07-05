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
import ru.nik.library.domain.Genre;
import ru.nik.library.dto.GenreDto;
import ru.nik.library.service.GenreService;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
    ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@AutoConfigureWebTestClient
class GenreFluxControllerTest {

    private WebTestClient webClient;

    @MockBean
    private GenreService service;

    private List<Genre> expected;


    @BeforeEach
    void setUp() {
        webClient = WebTestClient.bindToController(new GenreFluxController(service)).build();
        expected = new ArrayList<>();
        expected.add(new Genre("a","фантастика"));
        expected.add(new Genre("b","роман"));
    }

    @Test
    void getGenres() throws Exception {
        Mockito.when(service.getAllGenres()).thenReturn(Flux.just(new Genre("a","фантастика"), new Genre("b","роман")));

        this.webClient.get()
            .uri("/rest/genres")
            .accept(MediaType.APPLICATION_JSON_UTF8)
            .exchange()
            .expectStatus()
            .isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
            .expectBody()
            .json("[{\"id\":\"a\",\"name\":\"фантастика\",\"bookNames\":[]},"
                + "{\"id\":\"b\",\"name\":\"роман\",\"bookNames\":[]}]");

        verify(this.service, Mockito.atLeastOnce()).getAllGenres();
    }

    @Test
    void deleteGenre() throws Exception {
        Mockito.when(service.deleteGenreById("a")).thenReturn(Mono.empty());

        this.webClient.delete()
            .uri("/rest/genres/{id}", "a")
            .exchange()
            .expectStatus()
            .isOk()
            .expectBody().isEmpty();

        verify(this.service, Mockito.atLeastOnce()).deleteGenreById("a");
    }

    @Test
    void updateGenre() throws Exception {
        Mockito.when(service.updateGenre("a", "учебник")).thenReturn(Mono.just(new Genre("a", "учебник")));

        this.webClient.put()
            .uri("/rest/genres/{id}", "a")
            .contentType(MediaType.APPLICATION_JSON_UTF8)
            .body(Mono.just(new GenreDto("a", "учебник", Collections.emptyList())),
                GenreDto.class)
            .exchange().expectStatus().isOk()
            .expectBody()
            .json(asJsonString(new GenreDto("a", "учебник", Collections.emptyList())))
            .jsonPath("$.id").isNotEmpty()
            .jsonPath("$.name").isEqualTo("учебник");

        verify(this.service, Mockito.atLeastOnce()).updateGenre("a", "учебник");
    }

    @Test
    void addGenre() throws Exception {
        Mockito.when(service.addGenre("учебник")).thenReturn(Mono.just(new Genre("c", "учебник")));
        expected.add(new Genre("учебник"));

        this.webClient.post()
            .uri("/rest/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(new GenreDto(null, "учебник", Collections.emptyList())),
                GenreDto.class)
            .exchange().expectStatus().isOk()
            .expectBody().json(asJsonString(new GenreDto("c", "учебник", Collections.emptyList())))
            .jsonPath("$.id").isNotEmpty()
            .jsonPath("$.name").isEqualTo("учебник");

        verify(this.service, Mockito.atLeastOnce()).addGenre( "учебник");
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}