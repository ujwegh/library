package ru.nik.library.shell;

import java.util.List;
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
import ru.nik.library.service.AuthorServiceImpl;
import ru.nik.library.service.BookService;
import ru.nik.library.service.BookServiceImpl;
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
class BookShellControllerTest {

    @Autowired
    private BookService service;
    private BookShellController controller;

    @BeforeEach
    void init() {
        controller = new BookShellController(service);
        service.addBook("книга 1", "описание 1");
        service.addBook("книга 2", "описание 2");
    }

    @Test
    void books() {
        List<Book> books = service.getAllBooks();
        String result = controller.books();
        assertEquals("id: " + books.get(0).getId() + books.get(0).toString() + "\n" +
            "id: " + books.get(1).getId() + books.get(1).toString() + "\n", result);
    }

    @Test
    void getbook() {
        List<Book> books = service.getAllBooks();
        String result = controller.getbook(books.get(0).getId());
        assertEquals(books.get(0).toString(), result);
    }

    @Test
    void updatebook() {
        List<Book> books = service.getAllBooks();
        String result = controller.updatebook(books.get(0).getId(), "математика", "без проблем");
        assertEquals("Book with "+ books.get(0).getId() +" and математика successfully updated.", result);
    }

    @Test
    void deletebook() {
        List<Book> books = service.getAllBooks();
        String result = controller.deletebook(books.get(0).getId());
        assertEquals("Book with "+ books.get(0).getId() +" successfully deleted.", result);
    }

    @Test
    void addbook() {
        String result = controller.addbook("литература", "новая");
        assertEquals("Book with name литература successfully added.", result);
    }

    @Test
    void addbookauthors() {
        List<Book> books = service.getAllBooks();
        String result = controller.addbookauthors(books.get(0).getId(), "Кинг", "Желязны", "Толкин");
        assertEquals("Book with id: "+ books.get(0).getId() +" successfully updated. Updated authors with names: [Кинг, Желязны, Толкин]", result);
    }

    @Test
    void addbookgenres() {
        List<Book> books = service.getAllBooks();
        String result = controller.addbookauthors(books.get(0).getId(), "фантастика", "фентези", "роман");
        assertEquals("Book with id: "+ books.get(0).getId() +" successfully updated. Updated authors with names: [фантастика, фентези, роман]", result);

    }
}