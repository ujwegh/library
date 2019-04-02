package ru.nik.library.repository;

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
class AuthorDaoImplTest {

    @Autowired
    private AuthorDao dao;

    @Test
    void insertTest() {
        Author expected = new Author(null, "Вася Пупкин");
        dao.insert(expected);
        Author actual = dao.getByName("Вася Пупкин");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getByIdTest() {
        Author expected = new Author(null, "Пушкин");
        Author actual = dao.getById(1000);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getByNameTest() {
        Author expected = new Author(null, "Кинг");
        Author actual = dao.getByName("Кинг");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllTest() {
        List<Author> expected = new ArrayList<>();
        expected.add(new Author("Пушкин"));
        expected.add(new Author("Кинг"));
        List<Author> actual = dao.getAll();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void deleteByIdTest() {
        Author expected = new Author("Пушкин");
        dao.deleteById(1001);
        Author actual = dao.getAll().get(0);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteByNameTest() {
        Author expected = new Author("Пушкин");
        dao.deleteByName("Кинг");
        Author actual = dao.getAll().get(0);
        assertEquals(expected.getName(), actual.getName());
    }
}