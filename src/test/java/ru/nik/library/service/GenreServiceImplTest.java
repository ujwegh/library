package ru.nik.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
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
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreServiceImplTest {

    @Autowired
    private GenreService service;

    @BeforeEach
    public void init() {
        service.addGenre("сказки");
        service.addGenre("фантастика");
    }

    @Test
    void addGenreTest() {
        Genre expected = new Genre("сказки");
        service.addGenre(expected.getName());
        List<Genre> genres = service.getAllGenres();
        assertNotNull(genres);
        Genre actual = genres.get(2);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteGenreByIdTest() {
        service.deleteGenreById(1);
        List<Genre> genres = service.getAllGenres();
        assertNotNull(genres);
        assertEquals(1, genres.size());
        assertNull(service.getGenreById(1));
    }

    @Test
    void deleteGenreByNameTest() {
        service.deleteGenreByName("фантастика");
        List<Genre> genres = service.getAllGenres();
        assertNotNull(genres);
        assertEquals(1, genres.size());
        assertNull(service.getGenreByName("фантастика"));
    }

    @Test
    void updateGenreTest() {
        Genre expected = new Genre("ужасы");
        service.updateGenre(1, expected.getName());
        Genre actual = service.getGenreById(1);
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
        Genre actual = service.getGenreById(2);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllGenresTest() {
        List<Genre> expected = new ArrayList<>();
        expected.add(new Genre("фантастика"));
        expected.add(new Genre("сказки"));
        List<Genre> actual = service.getAllGenres();
        assertEquals(expected.size(), actual.size());
    }
}