package ru.nik.library.repository.datajpa;

//import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
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
@DataMongoTest
@TestPropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = AuthorRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository repository;

//    @Autowired
//    private EntityManager manager;

    @BeforeEach
    public void init() {
        Author one = new Author("abc","Пушкин");
        Author two = new Author("aaa","Кинг");
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
        Author actual = repository.findById(expected.getId());
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findById() {
        Author expected = new Author("abc","Пушкин");
        Author actual = repository.findById("abc");
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findByName() {
        Author expected = new Author( "Пушкин");
        Author actual = repository.findByName(expected.getName());
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
    }


    @Test
    void deleteById() {
        repository.deleteById("aaa");
        assertNull(repository.findById("aaa"));
    }

    @Test
    void deleteByName() {
        int i = repository.deleteByName("Пушкин");
        assertEquals(1, i);
        assertNull(repository.findByName("Пушкин"));
    }
}