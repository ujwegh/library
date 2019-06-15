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
import ru.nik.library.domain.Author;
import ru.nik.library.service.AuthorService;
import ru.nik.library.service.AuthorServiceImpl;
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
class AuthorShellControllerTest {

    @Autowired
    private AuthorService service;
    private AuthorShellController controller;

    @BeforeEach
    void init() {
        controller = new AuthorShellController(service);
        service.addAuthor("Достоевский");
        service.addAuthor("Лермонтов");
    }

    @Test
    void authors() {
        String result = controller.authors();
        List<Author> authors = service.getAllAuthors();
        System.out.println(result);
        assertEquals("id: " + authors.get(0).getId() + " " + authors.get(0).toString() + "\n" +
            "id: " + authors.get(1).getId() + " " + authors.get(1).toString() + "\n", result);
    }

    @Test
    void newauthor() {
        String result = controller.newauthor("Вася");
        assertEquals("Author Вася successfully added.", result);
    }

    @Test
    void updateauthor() {
        List<Author> authors = service.getAllAuthors();
        String result = controller.updateauthor(authors.get(0).getId(), "Вася");
        assertEquals(
            "Author with id: " + authors.get(0).getId() + " and name: Вася successfully updated.",
            result);
    }

    @Test
    void deleteauthorbyname() {
        String result = controller.deleteauthorbyname("Достоевский");
        assertEquals("Author Достоевский successfully deleted.", result);
    }

    @Test
    void deleteauthorbyid() {
        List<Author> authors = service.getAllAuthors();
        String result = controller.deleteauthorbyid(authors.get(0).getId());
        assertEquals("Author with id: " + authors.get(0).getId() + " successfully deleted.",
            result);
    }

    @Test
    void getauthorbyname() {
        List<Author> authors = service.getAllAuthors();
        String result = controller.getauthorbyname("Лермонтов");
        assertEquals(authors.get(1).toString(), result);
    }

    @Test
    void getauthorbyid() {
        List<Author> authors = service.getAllAuthors();
        String result = controller.getauthorbyid(authors.get(0).getId());
        assertEquals(authors.get(0).toString(), result);
    }
}