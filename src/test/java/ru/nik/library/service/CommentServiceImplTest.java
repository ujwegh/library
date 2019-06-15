package ru.nik.library.service;

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

import java.util.ArrayList;
import java.util.List;

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
class CommentServiceImplTest {

    @Autowired
    private CommentService service;

    @Autowired
    private BookService bookService;

    @BeforeEach
    void init() {
        bookService.addBook("книга 1", "описание 1");
        bookService.addBook("книга 2", "описание 2");
        List<Book> allBooks = bookService.getAllBooks();
        service.addComment(allBooks.get(0).getId(), "интересная книга");
        service.addComment(allBooks.get(0).getId(), "впринципе почитать можно");
        service.addComment(allBooks.get(1).getId(), "не интересная книга");
    }

    @Test
    void addComment() {
        Comment comment = new Comment("новый коментарий");
        List<Book> allBooks = bookService.getAllBooks();
        boolean b = service.addComment(allBooks.get(0).getId(), comment.getComment());
        assertTrue(b);
        List<Comment> comments = service.getAllComments(allBooks.get(0).getId());
        assertNotNull(comments);
        assertEquals(3, comments.size());
    }

    @Test
    void deleteCommentById() {
        List<Book> allBooks = bookService.getAllBooks();
        List<Comment> allComments = service.getAllComments(allBooks.get(0).getId());
        String bookId = allBooks.get(0).getId();
        String commentId = allComments.get(0).getId();

        boolean b = service.deleteCommentById(commentId, bookId);
        assertTrue(b);
        List<Comment> comments = service.getAllComments(bookId);
        assertEquals(1, comments.size());
        assertNull(service.getCommentById(commentId, bookId));
    }

    @Test
    void updateBookComment() {
        List<Book> allBooks = bookService.getAllBooks();
        List<Comment> allComments = service.getAllComments(allBooks.get(0).getId());
        String bookId = allBooks.get(0).getId();
        String commentId = allComments.get(0).getId();

        Comment comment = service.getCommentById(commentId, bookId);
        comment.setComment("измененный комент");
        boolean b = service.updateBookComment(commentId, bookId, comment.getComment());
        assertTrue(b);
        Comment actual = service.getCommentById(commentId, bookId);
        assertNotNull(actual);
        assertEquals(comment.toString(), actual.toString());
    }

    @Test
    void getCommentById() {
        List<Book> allBooks = bookService.getAllBooks();
        List<Comment> allComments = service.getAllComments(allBooks.get(0).getId());
        String bookId = allBooks.get(0).getId();
        String commentId = allComments.get(0).getId();

        Comment comment = new Comment(commentId, "интересная книга");
        Comment actual = service.getCommentById(commentId, bookId);
        assertNotNull(actual);
        assertEquals(comment.getComment(), actual.getComment());
    }

    @Test
    void getAllComments() {
        List<Book> allBooks = bookService.getAllBooks();
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("интересная книга"));
        comments.add(new Comment("не игтересная книга"));

        List<Comment> actual = service.getAllComments(allBooks.get(0).getId());
        assertNotNull(actual);
        assertEquals(comments.size(), actual.size());
    }
}