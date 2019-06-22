package ru.nik.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
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
@EnableMongoRepositories(basePackages = {"ru.nik.library.repository"})
@EnableAutoConfiguration
@ContextConfiguration(classes = {AuthorServiceImpl.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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
        Author actual = autors.get(2);
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void deleteAuthorByIdTest() {
        Author author = service.getAuthorByName("Достоевский");
        service.deleteAuthorById(author.getId());
        List<Author> authors = service.getAllAuthors();
        assertNotNull(authors);
        assertEquals(1, authors.size());
        assertNull(service.getAuthorById(author.getId()));
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
        Author expected = service.getAuthorByName("Достоевский");
        service.updateAuthor(expected.getId(), "Новый автор");
        Author actual = service.getAuthorById(expected.getId());
        assertEquals("Новый автор", actual.getName());
    }

    @Test
    void getAuthorByNameTest() {
        Author expected = new Author("Лермонтов");
        Author actual = service.getAuthorByName("Лермонтов");
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAuthorByIdTest() {
        Author expected = service.getAuthorByName("Достоевский");
        Author actual = service.getAuthorById(expected.getId());
        assertEquals(expected.getName(), actual.getName());
    }

    @Test
    void getAllTest() {
        List<Author> expected = new ArrayList<>();
        expected.add(new Author("Пушкин"));
        expected.add(new Author("Кинг"));
        List<Author> actual = service.getAllAuthors();
        assertEquals(expected.size(), actual.size());
    }
}