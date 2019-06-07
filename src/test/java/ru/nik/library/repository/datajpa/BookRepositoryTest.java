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
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = BookRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Autowired
    private EntityManager manager;

    @BeforeEach
    public void init() {
        Book one = new Book("книга", "интересная");
        Book two = new Book("журнал", "новый");
        manager.persist(one);
        manager.persist(two);
    }


    @Test
    void findAll() {
        List<Book> expected = new ArrayList<>();
        Book one = new Book(1,"книга", "интересная");
        Book two = new Book(2,"журнал", "новый");
        expected.add(one);
        expected.add(two);

        List<Book> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findById() {
        Book expected = new Book(1,"книга", "интересная");
        Book actual = repository.findById(1);
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void deleteById() {
        repository.deleteById(1);
        assertNull(repository.findById(1));
    }

    @Test
    void save() {
        Book expected = new Book("новая книга", "неизвестно");
        Book actual = repository.save(expected);
        assertNotNull(actual);
        expected.setId(3);
        assertEquals(3, repository.findAll().size());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void update() {
        Book book = repository.findById(1);
        book.setName("Новая книжка");
        book.setDescription("новое описание");
        Set<Author> authors = new HashSet<>();
        authors.add(new Author("Пушкин"));
        authors.add(new Author("Лермонтов"));
        book.setAuthors(authors);
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("вот это хренотаа"));
        comments.add(new Comment("советую почитать"));
        book.setComments(comments);
        Book actual = repository.save(book);
        assertNotNull(actual);
        assertEquals(book, actual);
    }
}