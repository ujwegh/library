package ru.nik.library.repository.datajpa;

import com.mongodb.MongoClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
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

    private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
        "testdb");

    @BeforeEach
    public void init() {
        Book one = new Book("aaa", "книга", "интересная");
        Book two = new Book("bbb", "журнал", "новый");
        mongoTemplate.dropCollection("books");
        mongoTemplate.save(one);
        mongoTemplate.save(two);
    }


    @Test
    void findAll() {
        List<Book> expected = new ArrayList<>();
        Book one = new Book("aaa", "книга", "интересная");
        Book two = new Book("bbb", "журнал", "новый");
        expected.add(one);
        expected.add(two);

        repository.findAll().collectList()
        .as(StepVerifier::create)
        .assertNext(books -> {
            assertNotNull(books);
            assertEquals(expected.size(), books.size());
            assertEquals(expected.toString(), books.toString());
        }).verifyComplete();
    }

    @Test
    void findById() {
        Book expected = new Book("aaa", "книга", "интересная");
        repository.findById("aaa")
        .as(StepVerifier::create)
        .assertNext(book -> {
            assertNotNull(book);
            assertEquals(expected.toString(), book.toString());
        }).verifyComplete();
    }

    @Test
    void deleteById() {
        repository.deleteById("bbb")
        .as(StepVerifier::create)
        .verifyComplete();

        assertNull(repository.findById("bbb").block());
    }

    @Test
    void save() {
        Book expected = new Book("ccc", "новая книга", "неизвестно");
        repository.save(expected)
        .as(StepVerifier::create)
        .assertNext(book -> {
            assertNotNull(book);
            assertEquals(expected.toString(), book.toString());
        }).verifyComplete();
    }

    @Test
    void update() {
        Set<Author> authors = new HashSet<>();
        authors.add(new Author("aa", "Пушкин"));
        authors.add(new Author("bb", "Лермонтов"));

        List<Comment> comments = new ArrayList<>();
        comments.add(new Comment("aa", "вот это хренотаа"));
        comments.add(new Comment("bb", "советую почитать"));

        Mono<Book> book = repository.findById("aaa").map(book1 -> {
            book1.setName("Новая книжка");
            book1.setDescription("новое описание");
            book1.setAuthors(authors);
            book1.setComments(comments);
            return book1;
        });

        Book expected = book.block();

        repository.save(expected)
        .as(StepVerifier::create)
        .assertNext(s -> {
            assertNotNull(s);
            assertEquals(expected, s);
        }).verifyComplete();

    }
}