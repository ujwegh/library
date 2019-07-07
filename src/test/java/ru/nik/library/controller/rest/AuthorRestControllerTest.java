package ru.nik.library.controller.rest;

import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.nik.library.domain.Author;
import ru.nik.library.dto.AuthorDto;
import ru.nik.library.security.AuthenticationSuccessHandlerImpl;
import ru.nik.library.service.AuthorService;
import ru.nik.library.service.UserService;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthorRestController.class)
class AuthorRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService service;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationSuccessHandlerImpl successHandler;

    private List<Author> expected;

    @BeforeEach
    void setUp() {
        expected = new ArrayList<>();
        expected.add(new Author(1, "Пушкин"));
        expected.add(new Author(2, "Кинг"));
    }

    @WithMockUser
    @Test
    void getAuthors() throws Exception {
        Mockito.when(service.getAllAuthors()).thenReturn(expected);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/authors")
            .accept(MediaType.APPLICATION_JSON_VALUE);
        mvc.perform(requestBuilder).andExpect(status().isOk())
            .andExpect(content().json("[{\"id\":1,\"name\":\"Пушкин\",\"bookNames\":[]},"
                + "{\"id\":2,\"name\":\"Кинг\",\"bookNames\":[]}]")).andReturn();
        verify(this.service, Mockito.atLeastOnce()).getAllAuthors();
    }

    @WithMockUser
    @Test
    void deleteAuthor() throws Exception {
        Mockito.when(service.deleteAuthorById(1)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/authors/{id}", 1)
            .with(csrf());
        this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        verify(this.service, Mockito.atLeastOnce()).deleteAuthorById(1);
    }

    @WithMockUser
    @Test
    void updateAuthor() throws Exception {
        Mockito.when(service.updateAuthor(1, "Лермонтов")).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/authors/{id}", 1)
            .with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new AuthorDto(1, "Лермонтов", Collections.emptyList())));
        this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        verify(this.service, Mockito.atLeastOnce()).updateAuthor(1, "Лермонтов");
    }

    @WithMockUser
    @Test
    void addAuthor() throws Exception {
        Mockito.when(service.addAuthor("Лермонтов")).thenReturn(true);
        expected.add(new Author("Лермонтов"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/authors").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new AuthorDto(0,"Лермонтов", Collections.emptyList())));
		this.mvc.perform(requestBuilder).andExpect(status().isOk())
            .andExpect(content().json("{\"id\":0,\"name\":\"Лермонтов\",\"bookNames\":[]}")).andReturn();
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