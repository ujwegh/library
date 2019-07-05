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

    private MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient("localhost", 27017),
        "testdb");

    @BeforeEach
    public void init() {
        Genre one = new Genre("abc", "жанр 1");
        Genre two = new Genre("aaa", "жанр 2");
        mongoTemplate.dropCollection("genres");
        mongoTemplate.save(one);
        mongoTemplate.save(two);
    }

    @Test
    void findAll() {
        List<Genre> genres = new ArrayList<>();
        Genre one = new Genre("abc", "жанр 1");
        Genre two = new Genre("aaa", "жанр 2");
        genres.add(one);
        genres.add(two);

        repository.findAll()
            .collectList()
            .as(StepVerifier::create)
            .assertNext(authors1 -> {
                assertNotNull(genres);
                assertEquals(2, genres.size());
            })
            .verifyComplete();
    }

    @Test
    void findAllByNameIn() {
        List<Genre> genres = new ArrayList<>();
        Genre one = new Genre("abc", "жанр 1");
        Genre two = new Genre("aaa", "жанр 2");
        genres.add(one);
        genres.add(two);

        repository.findAllByNameIn(one.getName(), two.getName())
            .collectList()
            .as(StepVerifier::create)
            .assertNext(authors1 -> {
                assertNotNull(genres);
                assertEquals(genres.size(), 2);
            })
            .verifyComplete();
    }

    @Test
    void save() {
        Genre expected = new Genre("Новый жанр");

        repository.save(expected)
            .as(StepVerifier::create)
            .assertNext(genre -> {
                assertNotNull(genre.getId());
                assertEquals(expected.getName(), genre.getName());
            })
            .verifyComplete();
    }

    @Test
    void update() {
        Mono<Genre> genreMono = repository.findById("abc").doOnSuccess(genre -> genre.setName("сказка"));

        Mono<Genre> updated = repository.save(genreMono.block());
        StepVerifier
            .create(updated)
            .assertNext(genre -> {
                assertNotNull(genre.getId());
                assertEquals("сказка", genre.getName());
            }).verifyComplete();
    }

    @Test
    void findById() {
        Genre expected = new Genre("abc", "жанр 1");

        repository.findById(expected.getId())
            .as(StepVerifier::create)
            .assertNext(genre -> {
                assertNotNull(genre);
                assertEquals(expected.toString(), genre.toString());
            }).verifyComplete();
    }

    @Test
    void findByName() {
        Genre expected = new Genre("жанр 1");

        repository.findByName(expected.getName())
            .as(StepVerifier::create)
            .assertNext(genre -> {
                assertNotNull(genre);
                assertEquals(expected.getName(), genre.getName());
            }).verifyComplete();
    }


    @Test
    void deleteById() {
        repository.deleteById("abc")
            .as(StepVerifier::create)
            .verifyComplete();

        assertNull(repository.findById("abc").block());
    }
}