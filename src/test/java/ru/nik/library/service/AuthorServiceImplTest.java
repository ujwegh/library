package ru.nik.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Author;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@AutoConfigureDataMongo
@TestPropertySource("classpath:application-test.properties")
@EnableMongoRepositories
@EnableAutoConfiguration
@ContextConfiguration(classes = AuthorService.class)
////@AutoConfigureTestDatabase
////@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorServiceImplTest {

    @Autowired
    private AuthorService service;

    @BeforeEach
    void init() {
        service.addAuthor("Достоевский");
        service.addAuthor("Лермонтов");
    }

    @Test
    void addAuthorTest() {
        Author expected = new Author("a","Толкин");
        service.addAuthor(expected.getName());
        List<Author> autors = service.getAllAuthors();
        assertNotNull(autors);
        Author actual = autors.get(1);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteAuthorByIdTest() {
//        service.deleteAuthorById(1);
//        List<Author> authors = service.getAllAuthors();
//        assertNotNull(authors);
//        assertEquals(1, authors.size());
//        assertNull(service.getAuthorById(1));
    }

    @Test
    void deleteAuthorByNameTest() {
        service.deleteAuthorByName("Достоевский");
        List<Author> authors = service.getAllAuthors();
        assertNotNull(authors);
        assertEquals(1, authors.size());
        assertNull(service.getAuthorByName("Достоевский"));
    }

    @Test
    void updateAuthorTest() {
        Author expected = new Author("Пушкин А.С.");
//        service.updateAuthor(1, expected.getName());
//        Author actual = service.getAuthorById(1);
//        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAuthorByNameTest() {
        Author expected = new Author("Лермонтов");
        Author actual = service.getAuthorByName("Лермонтов");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAuthorByIdTest() {
        Author expected = new Author("Лермонтов");
//        Author actual = service.getAuthorById(2);
//        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllTest() {
        List<Author> expected = new ArrayList<>();
        expected.add(new Author("Достоевский"));
        expected.add(new Author("Лермонтов"));
        List<Author> actual = service.getAllAuthors();
        assertEquals(expected.size(), actual.size());
    }
}