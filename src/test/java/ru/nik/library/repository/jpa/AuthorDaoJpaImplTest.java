package ru.nik.library.repository.jpa;

import org.junit.Before;
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
import ru.nik.library.repository.AuthorDao;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = AuthorDaoJpaImpl.class)
@EntityScan(basePackages = "ru.nik.library.domain")
@EnableAutoConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorDaoJpaImplTest {

    @Autowired
    private AuthorDao dao;

    @BeforeEach
    public void init() {
        Author one = new Author("Пушкин");
        Author two = new Author("Кинг");
        dao.insert(one);
        dao.insert(two);
    }

    @Test
    void insert() {
        Author author = new Author("Роджер");
        boolean b = dao.insert(author);
        int size = dao.getAll().size();
        assertTrue(b);
        assertEquals(3, size);
    }

    @Test
    void update() {
        Author expected = new Author(1, "Новый автор");
        Author author = dao.update(expected);
        assertNotNull(author);
        Author actual = dao.getByName(expected.getName());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getById() {
        Author expected = new Author("Пушкин");
        Author actual = dao.getById(1);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getByName() {
        Author expected = new Author("Кинг");
        Author actual = dao.getByName(expected.getName());
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAll() {
        Author one = new Author("Пушкин");
        Author two = new Author("Кинг");
        List<Author> expected = new ArrayList<>();
        expected.add(one);
        expected.add(two);
        List<Author> actual = dao.getAll();
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void deleteById() {
        boolean b = dao.deleteById(1);
        assertTrue(b);
        List<Author> authors = dao.getAll();
        assertNotNull(authors);
        assertEquals(1, authors.size());
    }

    @Test
    void deleteByName() {
        boolean b = dao.deleteByName("Пушкин");
        assertTrue(b);
        List<Author> authors = dao.getAll();
        assertNotNull(authors);
        assertEquals(1, authors.size());
    }

    @Test
    void getAllByNames() {
        Author one = new Author("Пушкин");
        Author two = new Author("Кинг");
        List<Author> expected = new ArrayList<>();
        expected.add(one);
        expected.add(two);
        List<Author> actual = dao.getAllByNames(one.getName(), two.getName());
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }
}