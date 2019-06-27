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
import ru.nik.library.domain.Author;
import ru.nik.library.dto.AuthorDto;
import ru.nik.library.service.AuthorService;

@RunWith(SpringRunner.class)
//@WebMvcTest(AuthorRestController.class)
class AuthorRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthorService service;

    private List<Author> expected;

    @BeforeEach
    void setUp() {
        expected = new ArrayList<>();
        expected.add(new Author("a", "Пушкин"));
        expected.add(new Author("b", "Кинг"));
    }


//    @Test
//    void getAuthors() throws Exception {
//        Mockito.when(service.getAllAuthors()).thenReturn(expected);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/rest/authors/all")
//            .accept(MediaType.APPLICATION_JSON_VALUE);
//        MvcResult result = mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
//        String expectedString = "[{\"id\":a,\"name\":\"Пушкин\",\"bookNames\":[]},{\"id\":b,\"name\":\"Кинг\",\"bookNames\":[]}]";
//        JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
//        verify(this.service, Mockito.atLeastOnce()).getAllAuthors();
//    }
//
//    @Test
//    void deleteAuthor() throws Exception {
//        Mockito.when(service.deleteAuthorById("a")).thenReturn(true);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/rest/authors/author/{id}", "a");
//        this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
//        verify(this.service, Mockito.atLeastOnce()).deleteAuthorById("a");
//    }
//
//    @Test
//    void updateAuthor() throws Exception {
//        Mockito.when(service.updateAuthor("a", "Лермонтов")).thenReturn(true);
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/rest/authors/author/{id}", "a")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(asJsonString(new AuthorDto("a", "Лермонтов", Collections.emptyList())));
//        this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
//        verify(this.service, Mockito.atLeastOnce()).updateAuthor("a", "Лермонтов");
//    }
//
//    @Test
//    void addAuthor() throws Exception {
//        Mockito.when(service.addAuthor("Лермонтов")).thenReturn(true);
//        expected.add(new Author("Лермонтов"));
//        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/rest/authors")
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(asJsonString(new AuthorDto("a","Лермонтов", Collections.emptyList())));
//		MvcResult result = this.mvc.perform(requestBuilder).andExpect(status().isOk()).andReturn();
//		String expectedString = "{\"id\":a,\"name\":\"Лермонтов\",\"bookNames\":[]}";
//		JSONAssert.assertEquals(expectedString, result.getResponse().getContentAsString(), false);
//        verify(this.service, Mockito.atLeastOnce()).addAuthor( "Лермонтов");
//    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}