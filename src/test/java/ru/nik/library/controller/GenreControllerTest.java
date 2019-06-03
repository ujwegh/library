package ru.nik.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.nik.library.domain.Genre;
import ru.nik.library.service.GenreService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@WebMvcTest(GenreController.class)
class GenreControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService service;

    private List<Genre> expected;


    @BeforeEach
    void setUp() {
        expected = new ArrayList<>();
        expected.add(new Genre("фантастика"));
        expected.add(new Genre("роман"));
    }

    @Test
    void getGenresTest() throws Exception {
        given(service.getAllGenres()).willReturn(expected);
        this.mvc.perform(get("/genres")).andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("genres", expected));
    }

    @Test
    void editTest() throws Exception {
        given(service.getGenreById(0)).willReturn(expected.get(0));
        this.mvc.perform(get("/genres/edit").param("id", "0")).andExpect(status().isOk())
                .andExpect(view().name("/edit"))
                .andExpect(model().attribute("genre", expected.get(0)));
    }

    @Test
    void deleteTest() throws Exception {
        given(service.deleteGenreById(0)).willReturn(true);
        given(service.getAllGenres()).willReturn(Collections.singletonList(expected.get(1)));
        this.mvc.perform(post("/genres/delete").param("id", "0")).andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("genres", Collections.singletonList(expected.get(1))));
    }

    @Test
    void addAuthorTest() throws Exception {
        given(service.addGenre("детектив")).willReturn(true);
        expected.add(new Genre("детектив"));
        given(service.getAllGenres()).willReturn(expected);
        this.mvc.perform(post("/genres").param("name", "детектив")).andExpect(status().isOk())
                .andExpect(view().name("list"))
                .andExpect(model().attribute("genres", expected));
    }

    @Test
    void updateAuthorTest() throws Exception {
        given(service.updateGenre(0, "детектив")).willReturn(true);
        expected.set(0, new Genre("детектив"));
        given(service.getAllGenres()).willReturn(expected);
        this.mvc.perform(post("/genres/update").param("id", "0").sessionAttr("name", "детектив"))
                .andExpect(status().isOk()).andExpect(view().name("list"))
                .andExpect(model().attribute("genres", expected));
    }
}