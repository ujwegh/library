package ru.nik.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

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
class BookDaoImplTest {

    @Autowired
    private BookDao dao;

    @Test
    void insertTest() {
        Book expected = new Book("физика", "учебник для студетов",
                new Author("Пушкин"), new Genre("фантастика"));
        dao.insert(expected);
        Book actual = dao.getAll().get(0);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void getByIdTest() {
        Book expected = new Book("сказки", "для взрослых",
                new Author("Кинг"), new Genre("детектив"));
        Book actual = dao.getById(1004);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void getAllTest() {
        List<Book> expected = new ArrayList<>();
        expected.add(new Book("сказки", "для взрослых",
                new Author("Кинг"), new Genre("детектив")));
        expected.add(new Book("сказки", "для детей",
                new Author("Пушкин"), new Genre("фантастика")));
        List<Book> actual = dao.getAll();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void deleteByIdTest() {
        Book expected = new Book("сказки", "для детей",
                new Author("Пушкин"), new Genre("фантастика"));
        dao.deleteById(1004);
        Book actual = dao.getAll().get(0);
        assertEquals(expected.toString(), actual.toString());
    }
}