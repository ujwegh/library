package ru.nik.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import ru.nik.library.domain.Comment;

//import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Transactional
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void init() {
        bookService.addBook("книга 1", "описание 1");
        bookService.addBook("книга 2", "описание 2");
//        service.addComment(1, "интересная книга");
//        service.addComment(1, "впринципе почитать можно");
//        service.addComment(2, "не интересная книга");
    }

    @Test
    void addComment() {
        Comment comment = new Comment("новый коментарий");
//        boolean b = service.addComment(1, comment.getComment());
//        assertTrue(b);
//        List<Comment> comments = service.getAllComments(1);
//        assertNotNull(comments);
//        assertEquals(3, comments.size());
    }

    @Test
    void deleteCommentById() {
//        boolean b = service.deleteCommentById(1, 1);
//        assertTrue(b);
//        List<Comment> comments = service.getAllComments(1);
//        assertEquals(1, comments.size());
//        assertNull(service.getCommentById(1,1));
    }

    @Test
    void updateBookComment() {
//        Comment comment = service.getCommentById(1, 1);
//        comment.setComment("измененный комент");
//        boolean b = service.updateBookComment(1, 1, comment.getComment());
//        assertTrue(b);
//        Comment actual = service.getCommentById(1, 1);
//        assertNotNull(actual);
//        assertEquals(comment, actual);
    }

    @Test
    void getCommentById() {
//        Comment comment = new Comment(1, "интересная книга");
//        Comment actual = service.getCommentById(1, 1);
//        assertNotNull(actual);
//        assertEquals(comment.getComment(), actual.getComment());
    }

    @Test
    void getAllComments() {
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("интересная книга"));
        comments.add(new Comment("не игтересная книга"));

//        List<Comment> actual = service.getAllComments(1);
//        assertNotNull(actual);
//        assertEquals(comments.size(), actual.size());
    }
}