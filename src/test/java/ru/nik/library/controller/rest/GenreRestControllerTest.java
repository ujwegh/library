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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.nik.library.domain.Genre;
import ru.nik.library.dto.GenreDto;
import ru.nik.library.service.GenreService;

@RunWith(SpringRunner.class)
@WebMvcTest(GenreRestController.class)
class GenreRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private GenreService service;

    private List<Genre> expected;


    @BeforeEach
    void setUp() {
        expected = new ArrayList<>();
        expected.add(new Genre(1,"фантастика"));
        expected.add(new Genre(2,"роман"));
    }

    @Test
    void getGenres() throws Exception {
        Mockito.when(service.getAllGenres()).thenReturn(expected);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/genres/all")
            .accept(MediaType.APPLICATION_JSON_VALUE);
        MvcResult result = mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        String expectedString = "[{\"id\":1,\"name\":\"фантастика\",\"bookNames\":[]},{\"id\":2,\"name\":\"роман\",\"bookNames\":[]}]";
        JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
        verify(this.service, Mockito.atLeastOnce()).getAllGenres();
    }

    @Test
    void deleteGenre() throws Exception {
        Mockito.when(service.deleteGenreById(1)).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/genres/genre/{id}", 1);
        this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        verify(this.service, Mockito.atLeastOnce()).deleteGenreById(1);
    }

    @Test
    void updateGenre() throws Exception {
        Mockito.when(service.updateGenre(1, "учебник")).thenReturn(true);
        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/genres/genre/{id}", 1)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new GenreDto(1, "учебник", Collections.emptyList())));
        this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
        verify(this.service, Mockito.atLeastOnce()).updateGenre(1, "учебник");
    }

    @Test
    void addGenre() throws Exception {
        Mockito.when(service.addGenre("учебник")).thenReturn(true);
        expected.add(new Genre("учебник"));
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJsonString(new GenreDto(0,"учебник", Collections.emptyList())));
        this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
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