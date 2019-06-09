package ru.nik.library.repository.datajpa;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Author;

@RunWith(SpringRunner.class)
@DataMongoTest
class AuthorRepositoryMongoTest {

    @Autowired
    private AuthorRepository repository;

    @BeforeEach
    public void init() {
        Author author = new Author("Вася");
        Author author2 = new Author("Петя");
        Author author3 = new Author("Коля");
        repository.save(author);
        repository.save(author2);
        repository.save(author3);

    }


    @Test
    void findAll() {
        List<Author> authors = repository.findAll();
        assertEquals(authors.size(), 2);
    }

    @Test
    void findAllByNameIn() {

    }

    @Test
    void findByName() {

    }

    @Test
    void findById() {

    }

    @Test
    void deleteById() {

    }

    @Test
    void deleteByName() {

    }
}