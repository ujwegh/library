package ru.nik.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableMongoRepositories(basePackages = {"ru.nik.library.repository"})
@EnableAutoConfiguration
@ContextConfiguration(classes = {GenreServiceImpl.class})
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
        Genre expected = new Genre("a","детектив");
        service.addGenre(expected.getName());
        List<Genre> genres = service.getAllGenres();
        assertNotNull(genres);
        Genre actual = genres.get(2);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteGenreByIdTest() {
        Genre genre = service.getGenreByName("сказки");
        service.deleteGenreById(genre.getId());
        List<Genre> genres = service.getAllGenres();
        assertNotNull(genres);
        assertEquals(1, genres.size());
        assertNull(service.getGenreById(genre.getId()));
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
        Genre expected = service.getGenreByName("сказки");
        service.updateGenre(expected.getId(), "учебник");
        Genre actual = service.getGenreById(expected.getId());
        assertEquals("учебник", actual.getName());
    }

    @Test
    void getGenreByNameTest() {
        Genre expected = new Genre("фантастика");
        Genre actual = service.getGenreByName("фантастика");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getGenreByIdTest() {
        Genre expected = service.getGenreByName("фантастика");
        Genre actual = service.getGenreById(expected.getId());
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