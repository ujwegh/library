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
import ru.nik.library.service.AuthorService;

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
public class AuthorShellControllerTest {

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
        System.out.println(result);
        assertEquals("id: 1 Author(id=1, name=Достоевский, books=[])\n"
                + "id: 2 Author(id=2, name=Лермонтов, books=[])\n", result);
    }

    @Test
    void newauthor() {
        String result = controller.newauthor("Вася");
        assertEquals("Author Вася successfully added.", result);
    }

    @Test
    void updateauthor() {
        String result = controller.updateauthor(1, "Вася");
        assertEquals("Author with id: 1 and name: Вася successfully updated.", result);
    }

    @Test
    void deleteauthorbyname() {
        String result = controller.deleteauthorbyname("Достоевский");
        assertEquals("Author Достоевский successfully deleted.", result);
    }

    @Test
    void deleteauthorbyid() {
        String result = controller.deleteauthorbyid(1);
        assertEquals("Author with id: 1 successfully deleted.", result);
    }

    @Test
    void getauthorbyname() {
        String result = controller.getauthorbyname("Лермонтов");
        assertEquals("Author(id=2, name=Лермонтов, books=[])", result);
    }

    @Test
    void getauthorbyid() {
        String result = controller.getauthorbyid(1);
        assertEquals("Author(id=1, name=Достоевский, books=[])", result);
    }
}