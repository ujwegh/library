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
import ru.nik.library.domain.Genre;
import ru.nik.library.service.AuthorServiceImpl;
import ru.nik.library.service.BookServiceImpl;
import ru.nik.library.service.CommentServiceImpl;
import ru.nik.library.service.GenreService;
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
class GenreShellControllerTest {

    @Autowired
    private GenreService service;
    private GenreShellController controller;

    @BeforeEach
    void init() {
        controller = new GenreShellController(service);
        service.addGenre("жанр 1");
        service.addGenre("жанр 2");
    }

    @Test
    void genres() {
        List<Genre> genres = service.getAllGenres();
        String result = controller.genres();
        assertEquals("id: " + genres.get(0).getId() + " " + genres.get(0).toString() + "\n" +
            "id: " + genres.get(1).getId() + " " + genres.get(1).toString() + "\n", result);
    }

    @Test
    void newgenre() {
        String result = controller.newgenre("роман");
        assertEquals("Genre роман successfully added.", result);
    }

    @Test
    void updategenre() {
        List<Genre> genres = service.getAllGenres();
        String result = controller.updategenre(genres.get(0).getId(), "роман");
        assertEquals("Genre with " + genres.get(0).getId() + " and роман successfully updated.",
            result);
    }

    @Test
    void deletegenrebyname() {
        String result = controller.deletegenrebyname("жанр 1");
        assertEquals("Genre жанр 1 successfully deleted.", result);
    }

    @Test
    void deletegenrebyid() {
        List<Genre> genres = service.getAllGenres();
        String result = controller.deletegenrebyid(genres.get(0).getId());
        assertEquals("Genre with " + genres.get(0).getId() + " successfully deleted.", result);
    }

    @Test
    void getgenrebyname() {
        List<Genre> genres = service.getAllGenres();
        String result = controller.getgenrebyname("жанр 2");
        assertEquals(genres.get(1).toString(), result);
    }

    @Test
    void getgenrebyid() {
        List<Genre> genres = service.getAllGenres();
        String result = controller.getgenrebyid(genres.get(1).getId());
        assertEquals(genres.get(1).toString(), result);
    }
}