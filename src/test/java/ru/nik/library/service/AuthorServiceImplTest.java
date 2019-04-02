package ru.nik.library.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.nik.library.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
})
class AuthorServiceImplTest {

    @Autowired
    private AuthorService service;

    @Test
    void addAuthorTest() {
        Author expected = new Author("Толкин");
        service.addAuthor(expected.getName());
        Author actual = service.getAllAutors().get(2);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteAuthorByIdTest() {
        Author expected = new Author("Пушкин");
        service.deleteAuthorById(1001);
        Author actual = service.getAllAutors().get(0);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteAuthorByNameTest() {
        Author expected = new Author("Пушкин");
        service.deleteAuthorByName("Кинг");
        Author actual = service.getAllAutors().get(0);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void updateAuthorTest() {
        Author expected = new Author("Пушкин А.С.");
        service.updateAuthor(1000, expected.getName());
        Author actual = service.getAllAutors().get(1);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAuthorByNameTest() {
        Author expected = new Author("Пушкин");
        Author actual = service.getAuthorByName("Пушкин");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAuthorByIdTest() {
        Author expected = new Author("Пушкин");
        Author actual = service.getAuthorById(1000);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllTest() {
        List<Author> expected = new ArrayList<>();
        expected.add(new Author("Пушкин"));
        expected.add(new Author("Кинг"));
        List<Author> actual = service.getAllAutors();
        assertEquals(expected.size(), actual.size());
    }
}