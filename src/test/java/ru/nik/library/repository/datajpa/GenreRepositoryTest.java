package ru.nik.library.repository.datajpa;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = GenreRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @Autowired
    private EntityManager manager;

    @BeforeEach
    public void init() {
        Genre one = new Genre("жанр 1");
        Genre two = new Genre("жанр 2");
        manager.persist(one);
        manager.persist(two);
    }

    @Test
    void findAll() {
        List<Genre> expected = new ArrayList<>();
        Genre one = new Genre("жанр 1");
        Genre two = new Genre("жанр 2");
        expected.add(one);
        expected.add(two);

        List<Genre> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void findAllByNameIn() {
        List<Genre> expected = new ArrayList<>();
        Genre one = new Genre("жанр 1");
        Genre two = new Genre("жанр 2");
        expected.add(one);
        expected.add(two);

        List<Genre> actual = repository.findAllByNameIn(one.getName(), two.getName());
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void save() {
        Genre expected = new Genre("Новый жанр");
        Genre actual = repository.save(expected);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        List<Genre> authors = repository.findAll();
        assertEquals(3, authors.size());
    }

    @Test
    void update() {
        Genre expected = repository.findById(1);;
        expected.setName("жанр 3");
        repository.save(expected);
        Genre actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findById() {
        Genre expected = new Genre(1, "жанр 1");
        Genre actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findByName() {
        Genre expected = new Genre(1, "жанр 1");
        Genre actual = repository.findByName(expected.getName());
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }


    @Test
    void deleteById() {
        repository.deleteById(1);
        assertNull(repository.findById(1));
    }

    @Test
    void deleteByName() {
        int i = repository.deleteByName("жанр 1");
        assertEquals(1, i);
        assertNull(repository.findByName("жанр 1"));
    }
}