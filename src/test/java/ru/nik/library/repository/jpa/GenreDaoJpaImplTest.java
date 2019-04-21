package ru.nik.library.repository.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.GenreDao;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = GenreDaoJpaImpl.class)
@EntityScan(basePackages = "ru.nik.library.domain")
@EnableAutoConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreDaoJpaImplTest {

    @Autowired
    private GenreDao dao;

    @BeforeEach
    public void init() {
        Genre one = new Genre("учебник");
        Genre two = new Genre("справочник");
        dao.insert(one);
        dao.insert(two);
    }

    @Test
    void insert() {
        Genre genre = new Genre("учебник");
        int i = dao.insert(genre);
        int size = dao.getAll().size();
        assertEquals(1, i);
        assertEquals(3, size);
    }

    @Test
    void update() {
        Genre expected = new Genre(1, "Новый жанр");
        int i = dao.insert(expected);
        Genre actual = dao.getByName(expected.getName());
        assertEquals(2, i);
        assertEquals(expected.getName(), actual.getName());
    }


    @Test
    void getById() {
        Genre expected = new Genre("учебник");
        Genre actual = dao.getById(1);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getByName() {
        Genre expected = new Genre("учебник");
        Genre actual = dao.getByName(expected.getName());
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAll() {
        Genre one = new Genre("учебник");
        Genre two = new Genre("справочник");
        List<Genre> expected = new ArrayList<>();
        expected.add(one);
        expected.add(two);
        List<Genre> actual = dao.getAll();
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void deleteById() {
        int i = dao.deleteById(1);
        assertEquals(1, i);
        List<Genre> genres = dao.getAll();
        assertNotNull(genres);
        assertEquals(1, genres.size());
    }

    @Test
    void deleteByName() {
        int i = dao.deleteByName("учебник");
        assertEquals(1, i);
        List<Genre> genres = dao.getAll();
        assertNotNull(genres);
        assertEquals(1, genres.size());
    }
}