package ru.nik.library.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
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
class GenreDaoImplTest {

    @Autowired
    private GenreDao dao;

    @Test
    void insertTest() {
        Genre expected = new Genre(null,"фантастика");
        dao.insert(expected);
        Genre actual = dao.getByName("фантастика");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getByIdTest() {
        Genre expected = new Genre(null,"фантастика");
        Genre actual = dao.getById(1002);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getByNameTest() {
        Genre expected = new Genre(null,"фантастика");
        Genre actual = dao.getByName("фантастика");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllTest() {
        List<Genre> expected = new ArrayList<>();
        expected.add(new Genre("фантастика"));
        expected.add(new Genre("детектив"));
        List<Genre> actual = dao.getAll();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void deleteByIdTest() {
        Genre expected = new Genre("детектив");
        dao.deleteById(1002);
        Genre actual = dao.getAll().get(0);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteByNameTest() {
        Genre expected = new Genre("детектив");
        dao.deleteByName("фантастика");
        Genre actual = dao.getAll().get(0);
        assertEquals(expected.getName(), actual.getName());
    }
}