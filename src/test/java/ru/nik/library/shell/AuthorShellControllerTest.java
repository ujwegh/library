package ru.nik.library.shell;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.nik.library.service.AuthorService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:postgreschema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data-test.sql")
})
class AuthorShellControllerTest {

    @Autowired
    private AuthorService service;
    private AuthorShellController controller;

    @BeforeEach
    void init() {
        controller = new AuthorShellController(service);
    }

    @Test
    void authors() {
        String result = controller.authors();
        assertEquals("id: 1000 Author(name=Пушкин)\n" +"id: 1001 Author(name=Кинг)\n", result);
    }

    @Test
    void newauthor() {
        String result = controller.newauthor("Вася");
        assertEquals("Author Вася successfully added.", result);
    }

    @Test
    void updateauthor() {
        String result = controller.updateauthor(1000, "Вася");
        assertEquals("Author with id: 1000 and name: Вася successfully updated.", result);
    }

    @Test
    void deleteauthorbyname() {
        String result = controller.deleteauthorbyname("Кинг");
        assertEquals("Author Кинг successfully deleted.", result);
    }

    @Test
    void deleteauthorbyid() {
        String result = controller.deleteauthorbyid(1000);
        assertEquals("Author with id: 1000 successfully deleted.", result);
    }

    @Test
    void getauthorbyname() {
        String result = controller.getauthorbyname("Кинг");
        assertEquals("Author(name=Кинг)", result);
    }

    @Test
    void getauthorbyid() {
        String result = controller.getauthorbyid(1001);
        assertEquals("Author(name=Кинг)", result);
    }
}