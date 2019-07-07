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
import ru.nik.library.domain.Author;
import ru.nik.library.security.AuthenticationSuccessHandlerImpl;
import ru.nik.library.service.AuthorService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import ru.nik.library.service.UserService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

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
        expected.add(new Author("Пушкин"));
        expected.add(new Author("Кинг"));
    }

    @WithMockUser
    @Test
    void getAuthorsTest() throws Exception {
        given(service.getAllAuthors()).willReturn(expected);
        this.mvc.perform(get("/authors")).andExpect(status().isOk())
            .andExpect(view().name("list"))
            .andExpect(model().attribute("authors", expected));
        verify(this.service, Mockito.atLeastOnce()).getAllAuthors();
    }

    @WithMockUser
    @Test
    void editTest() throws Exception {
        given(service.getAuthorById(0)).willReturn(expected.get(0));
        this.mvc.perform(get("/authors/edit/{id}", "0")).andExpect(status().isOk())
            .andExpect(view().name("/edit"))
            .andExpect(model().attribute("author", expected.get(0)));
        verify(this.service, Mockito.atLeastOnce()).getAuthorById(0);
    }

    @WithMockUser
    @Test
    void deleteTest() throws Exception {
        given(service.deleteAuthorById(0)).willReturn(true);
        given(service.getAllAuthors()).willReturn(Collections.singletonList(expected.get(1)));
        this.mvc.perform(post("/authors/delete").param("id", "0").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/authors")).andExpect(redirectedUrl("/authors"));
        verify(this.service, Mockito.atLeastOnce()).deleteAuthorById(0);
    }

    @WithMockUser
    @Test
    void addAuthorTest() throws Exception {
        given(service.addAuthor("King")).willReturn(true);
        expected.add(new Author("king"));
        given(service.getAllAuthors()).willReturn(expected);
        this.mvc.perform(post("/authors").param("name", "King").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/authors")).andExpect(redirectedUrl("/authors"));
        verify(this.service, Mockito.atLeastOnce()).addAuthor("King");
    }

    @WithMockUser
    @Test
    void updateAuthorTest() throws Exception {
        given(service.updateAuthor(0, "King")).willReturn(true);
        expected.set(0, new Author("King"));
        given(service.getAllAuthors()).willReturn(expected);
        this.mvc.perform(post("/authors/update").param("id", "0")
            .sessionAttr("name", "King").with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/authors"));
        verify(this.service, Mockito.atLeastOnce()).updateAuthor(0, "");
    }
}