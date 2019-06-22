package ru.nik.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.service.AuthorServiceImpl;
import ru.nik.library.service.BookService;
import ru.nik.library.service.BookServiceImpl;
import ru.nik.library.service.CommentService;

import java.util.List;
import ru.nik.library.service.CommentServiceImpl;
import ru.nik.library.service.GenreServiceImpl;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
    ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableMongoRepositories(basePackages = {"ru.nik.library.repository"})
@EnableAutoConfiguration
@ContextConfiguration(classes = {BookServiceImpl.class, AuthorServiceImpl.class,
    GenreServiceImpl.class, CommentServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Transactional
class CommentShellControllerTest {

    @Autowired
    private CommentService service;
    @Autowired
    private BookService bookService;
    private CommentShellController controller;

    @BeforeEach
    void init() {
        controller = new CommentShellController(service);
        bookService.addBook("книга", "описание");
        List<Book> books = bookService.getAllBooks();
        service.addComment(books.get(0).getId(), "комент 1");
        service.addComment(books.get(0).getId(), "комент 2");
    }


    @Test
    void allcomments() {
        List<Book> books = bookService.getAllBooks();
        List<Comment> comments = service.getAllComments(books.get(0).getId());

        String result = controller.allcomments(books.get(0).getId());
        assertEquals("Comments for book with id: " + books.get(0).getId() + " are " +
            comments.toString(), result);
    }

    @Test
    void getcomment() {
        List<Book> books = bookService.getAllBooks();
        List<Comment> comments = service.getAllComments(books.get(0).getId());

        String result = controller.getcomment(comments.get(0).getId(), books.get(0).getId());
        assertEquals(comments.get(0).toString(), result);
    }

    @Test
    void addcomment() {
        List<Book> books = bookService.getAllBooks();

        String result = controller.addcomment(books.get(0).getId(), "новый коммент");
        assertEquals("Comment новый коммент successfully added.", result);
    }

    @Test
    void updatecomment() {
        List<Book> books = bookService.getAllBooks();
        List<Comment> comments = service.getAllComments(books.get(0).getId());

        String result = controller
            .updatecomment(comments.get(0).getId(), books.get(0).getId(), "переделанный коммент");
        assertEquals("Comment with id: " + comments.get(0).getId()
            + " and book with id: " + books.get(0).getId() + " successfully updated.", result);
    }

    @Test
    void deletecomment() {
        List<Book> books = bookService.getAllBooks();
        List<Comment> comments = service.getAllComments(books.get(0).getId());

        String result = controller.deletecomment(comments.get(0).getId(), books.get(0).getId());
        assertEquals(
            "Comment with id: " + comments.get(0).getId() + " and book id: " + books.get(0).getId()
                + " successfully deleted.", result);
    }
}