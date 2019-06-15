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
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = BookRepository.class)
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;


    @BeforeEach
    public void init() {
        Book one = new Book("aaa", "книга", "интересная");
        Book two = new Book("bbb", "журнал", "новый");
        repository.save(one);
        repository.save(two);
    }


    @Test
    void findAll() {
        List<Book> expected = new ArrayList<>();
        Book one = new Book("aaa", "книга", "интересная");
        Book two = new Book("bbb", "журнал", "новый");
        expected.add(one);
        expected.add(two);

        List<Book> actual = repository.findAll();
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void findById() {
        Book expected = new Book("aaa", "книга", "интересная");
        Book actual = repository.findById("aaa");
        assertNotNull(actual);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void deleteById() {
        repository.deleteById("bbb");
        assertNull(repository.findById("bbb"));
    }

    @Test
    void save() {
        Book expected = new Book("ccc", "новая книга", "неизвестно");
        Book actual = repository.save(expected);
        assertNotNull(actual);
        assertEquals(3, repository.findAll().size());
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void update() {
        Book book = repository.findById("aaa");
        book.setName("Новая книжка");
        book.setDescription("новое описание");
        Set<Author> authors = new HashSet<>();
        authors.add(new Author("aa", "Пушкин"));
        authors.add(new Author("bb", "Лермонтов"));
        book.setAuthors(authors);
        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("aa", "вот это хренотаа"));
        comments.add(new Comment("bb", "советую почитать"));
        book.setComments(comments);
        Book actual = repository.save(book);
        assertNotNull(actual);
        assertEquals(book, actual);
    }
}