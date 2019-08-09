package ru.nik.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import ru.nik.library.domain.Comment;
import ru.nik.library.service.BookService;
import ru.nik.library.service.CommentService;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class CommentShellControllerTest {

    @Autowired
    private CommentService service;
    @Autowired
    private BookService bookService;
    private CommentShellController controller;

    @BeforeEach
    void init() {
        controller = new CommentShellController(service);
        bookService.addBook("книга", "описание");
        service.addComment(1, "комент 1");
        service.addComment(1, "комент 2");
    }


    @Test
    void allcomments() {
        String result = controller.allcomments(1);
        assertEquals("Comments for book with id: 1 are " +
                "[Comment(id=1, comment=комент 1, book=Book{id=1, name='книга', " +
                "description='описание', comments=null, authors=0, genres=0}), " +
                "Comment(id=2, comment=комент 2, book=Book{id=1, name='книга', " +
                "description='описание', comments=null, authors=0, genres=0})]", result);
    }

    @Test
    void getcomment() {
        String result = controller.getcomment(1, 1);
        assertEquals("Comment(id=1, comment=комент 1, book=Book{id=1, name='книга', " +
                "description='описание', comments=null, authors=0, genres=0})", result);
    }

    @Test
    void addcomment() {
        String result = controller.addcomment(1, "новый коммент");
        assertEquals("Comment новый коммент successfully added.", result);
    }

    @Test
    void updatecomment() {
        String result = controller.updatecomment(1, 1, "переделанный коммент");
        assertEquals("Comment with id: 1 and book with id: 1 successfully updated.", result);
    }

    @Test
    void deletecomment() {
        String result = controller.deletecomment(1, 1);
        assertEquals("Comment with id: 1 and book id: 1 successfully deleted.", result);
    }
}