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
import ru.nik.library.service.BookService;

//import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Transactional
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
        String result = controller.books();
        assertEquals("id: 1Book{id=1, name='книга 1', description='описание 1', comments=null, authors=0, genres=0}\n" +
                "id: 2Book{id=2, name='книга 2', description='описание 2', comments=null, authors=0, genres=0}\n", result);
    }

    @Test
    void getbook() {
//        String result = controller.getbook(1);
//        assertEquals("Book{id=1, name='книга 1', description='описание 1', comments=null, authors=0, genres=0}", result);
    }

    @Test
    void updatebook() {
//        String result = controller.updatebook(1, "математика", "без проблем");
//        assertEquals("Book with 1 and математика successfully updated.", result);
    }

    @Test
    void deletebook() {
//        String result = controller.deletebook(1);
//        assertEquals("Book with 1 successfully deleted.", result);
    }

    @Test
    void addbook() {
        String result = controller.addbook("литература", "новая");
        assertEquals("Book with name литература successfully added.", result);
    }

    @Test
    void addbookauthors() {
//        String result = controller.addbookauthors(1, "Кинг", "Желязны", "Толкин");
//        assertEquals("Book with id: 1 successfully updated. Updated authors with names: [Кинг, Желязны, Толкин]", result);
    }

    @Test
    void addbookgenres() {
//        String result = controller.addbookauthors(1, "фантастика", "фентези", "роман");
//        assertEquals("Book with id: 1 successfully updated. Updated authors with names: [фантастика, фентези, роман]", result);


    }
}