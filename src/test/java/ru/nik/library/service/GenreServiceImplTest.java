package ru.nik.library.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.GenreDao;

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
class GenreServiceImplTest {

    @Autowired
    private GenreService service;

    @Test
    void addGenreTest() {
        Genre expected = new Genre("сказки");
        service.addGenre(expected.getName());
        Genre actual = service.getAllGenres().get(2);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteGenreByIdTest() {
        Genre expected = new Genre("фантастика");
        service.deleteGenreById(1004);
        Genre actual = service.getAllGenres().get(0);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteGenreByNameTest() {
        Genre expected = new Genre("фантастика");
        service.deleteGenreByName("детектив");
        Genre actual = service.getAllGenres().get(0);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void updateGenreTest() {
        Genre expected = new Genre("ужасы");
        service.updateGenre(1003, expected.getName());
        Genre actual = service.getAllGenres().get(1);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getGenreByNameTest() {
        Genre expected = new Genre("фантастика");
        Genre actual = service.getGenreByName("фантастика");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getGenreByIdTest() {
        Genre expected = new Genre("фантастика");
        Genre actual = service.getGenreById(1002);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllGenresTest() {
        List<Genre> expected = new ArrayList<>();
        expected.add(new Genre("фантастика"));
        expected.add(new Genre("детектив"));
        List<Genre> actual = service.getAllGenres();
        assertEquals(expected.size(), actual.size());
    }
}