package ru.nik.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.nik.library.service.GenreService;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
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
        String result = controller.genres();
        assertEquals("id: 1 Genre(id=1, name=жанр 1, books=[])\n"
                + "id: 2 Genre(id=2, name=жанр 2, books=[])\n", result);
    }

    @Test
    void newgenre() {
        String result = controller.newgenre("роман");
        assertEquals("Genre роман successfully added.", result);
    }

    @Test
    void updategenre() {
        String result = controller.updategenre(1, "роман");
        assertEquals("Genre with 1 and роман successfully updated.", result);
    }

    @Test
    void deletegenrebyname() {
        String result = controller.deletegenrebyname("жанр 1");
        assertEquals("Genre жанр 1 successfully deleted.", result);
    }

    @Test
    void deletegenrebyid() {
        String result = controller.deletegenrebyid(2);
        assertEquals("Genre with 2 successfully deleted.", result);
    }

    @Test
    void getgenrebyname() {
        String result = controller.getgenrebyname("жанр 2");
        assertEquals("Genre(id=2, name=жанр 2, books=[])", result);
    }

    @Test
    void getgenrebyid() {
        String result = controller.getgenrebyid(2);
        assertEquals("Genre(id=2, name=жанр 2, books=[])", result);
    }
}