package ru.nik.library.repository.datajpa;

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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = AuthorRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

    @BeforeEach
    public void init() {
        Author one = new Author("Пушкин");
        Author two = new Author("Кинг");
        repository.save(one);
        repository.save(two);
    }

    @Test
    void findAll() {
        List<Author> expected = new ArrayList<>();
        Author one = new Author("Пушкин");
        Author two = new Author("Кинг");
        expected.add(one);
        expected.add(two);

        List<Author> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void findAllByNameIn() {
        List<Author> expected = new ArrayList<>();
        Author one = new Author("Пушкин");
        Author two = new Author("Кинг");
        expected.add(one);
        expected.add(two);

        List<Author> actual = repository.findAllByNameIn(one.getName(), two.getName());
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void save() {
        Author expected = new Author("Новый автор");
        Author actual = repository.save(expected);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        List<Author> authors = repository.findAll();
        assertEquals(3, authors.size());
    }

    @Test
    void update() {
        Author expected = repository.findByName("Пушкин");
        expected.setName("Гоголь");
        repository.save(expected);
        Author actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findById() {
        Author expected = new Author(1, "Пушкин");
        Author actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findByName() {
        Author expected = new Author(1, "Пушкин");
        Author actual = repository.findByName(expected.getName());
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
        int i = repository.deleteByName("Пушкин");
        assertEquals(1, i);
        assertNull(repository.findByName("Пушкин"));
    }
}