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
import ru.nik.library.service.BookService;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
})
class BookShellControllerTest {

    @Autowired
    private BookService service;
    private BookShellController controller;

    @BeforeEach
    void init() {
        controller = new BookShellController(service);
    }

    @Test
    void books() {
        String result = controller.books();
        assertEquals("id: 1004Book(name=сказки, description=для взрослых, author=Author(name=Кинг), genre=Genre(name=детектив))\n" +
                "id: 1005Book(name=сказки, description=для детей, author=Author(name=Пушкин), genre=Genre(name=фантастика))\n", result);
    }

    @Test
    void getbook() {
        String result = controller.getbook(1004);
        assertEquals("Book(name=сказки, description=для взрослых, author=Author(name=Кинг), genre=Genre(name=детектив))", result);
    }

    @Test
    void updatebook() {
        String result = controller.updatebook(1004, "математика", "без проблем", "Попов", "учебник");
        assertEquals("Book with 1004 and математика successfully updated.", result);
    }

    @Test
    void deletebook() {
        String result = controller.deletebook(1004);
        assertEquals("Book with 1004 successfully deleted.", result);
    }

    @Test
    void addbook() {
        String result = controller.addbook("литература", "новая", "Донцова", "детектив");
        assertEquals("Book with литература successfully added.", result);
    }
}