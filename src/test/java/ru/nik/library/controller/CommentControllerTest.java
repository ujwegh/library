package ru.nik.library.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.service.CommentService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CommentService service;

    private List<Comment> expected;

    @BeforeEach
    void setUp() {
        expected = new ArrayList<>();
        Comment one = new Comment("какой-то коммент 1!!1?");
        Comment two = new Comment("другой какой-то коммент");
        Book book = new Book("книга", "описание");
//        book.setId(0);
        one.setBook(book);
        two.setBook(book);
        expected.add(one);
        expected.add(two);
    }


    @Test
    void getComments() throws Exception {
//        given(service.getAllComments(0)).willReturn(expected);
        this.mvc.perform(get("/comments/{bookId}", "0"))
                .andExpect(status().isOk()).andExpect(view().name("comments"))
                .andExpect(model().attribute("bookId", 0))
                .andExpect(model().attribute("comments", expected));
//        verify(this.service, Mockito.atLeastOnce()).getAllComments( 0);
    }

    @Test
    void edit() throws Exception {
//        given(service.getCommentById(0, 0)).willReturn(expected.get(0));
        this.mvc.perform(get("/comments/{bookId}/edit/{id}","0", "0"))
            .andExpect(status().isOk()).andExpect(view().name("/edit"))
                .andExpect(model().attribute("bookId", 0))
                .andExpect(model().attribute("comment", expected.get(0)));
//        verify(this.service, Mockito.atLeastOnce()).getCommentById(0, 0);
    }

    @Test
    void delete() throws Exception {
//        given(service.deleteCommentById(0, 0)).willReturn(true);
        this.mvc.perform(post("/comments/{bookId}/delete", "0").param("id", "0"))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/comments/0"))
                .andExpect(redirectedUrl("/comments/0"));
//        verify(this.service, Mockito.atLeastOnce()).deleteCommentById(0, 0);
    }

    @Test
    void addComment() throws Exception {
        expected.add(new Comment("новый коммент"));
//        given(service.addComment(0, "новый коммент")).willReturn(true);
        this.mvc.perform(post("/comments/{bookId}", "0").param("comment", "новый коммент"))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/comments/0"))
        .andExpect(redirectedUrl("/comments/0"));
//        verify(this.service, Mockito.atLeastOnce()).addComment(0, "новый коммент");
    }

    @Test
    void updateAuthor() throws Exception {
        expected.set(0, new Comment("обновленный коммент"));
//        given(service.updateBookComment(0, 0, "обновленный коммент")).willReturn(true);
//        given(service.getAllComments(0)).willReturn(expected);
        this.mvc.perform(post("/comments/{bookId}/update", "0").param("id", "0"))
                .andExpect(status().is3xxRedirection()).andExpect(view().name("redirect:/comments/0"))
                .andExpect(redirectedUrl("/comments/0"));
//        verify(this.service, Mockito.atLeastOnce()).updateBookComment(0, 0, "");
    }
}