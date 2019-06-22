package ru.nik.library.repository.datajpa;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = GenreRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
class GenreRepositoryTest {

    @Autowired
    private GenreRepository repository;

    @BeforeEach
    public void init() {
        Genre one = new Genre("abc", "жанр 1");
        Genre two = new Genre("aaa", "жанр 2");
        repository.save(one);
        repository.save(two);
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
        Genre expected = repository.findById("aaa");
        ;
        expected.setName("жанр 3");
        repository.save(expected);
        Genre actual = repository.findById("aaa");
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findById() {
        Genre expected = new Genre("abc", "жанр 1");
        Genre actual = repository.findById("abc");
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findByName() {
        Genre expected = new Genre("abc", "жанр 1");
        Genre actual = repository.findByName(expected.getName());
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }


    @Test
    void deleteById() {
        repository.deleteById("aaa");
        assertNull(repository.findById("aaa"));
    }

    @Test
    void deleteByName() {
        int i = repository.deleteByName("жанр 1");
        assertEquals(1, i);
        assertNull(repository.findByName("жанр 1"));
    }
}