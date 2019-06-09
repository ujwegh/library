package ru.nik.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

//import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableAutoConfiguration
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @BeforeEach
    void init() {
        Book one = new Book("книга 1", "описание");
        Book two = new Book("книга 2", "описание");
        service.addBook(one.getName(), one.getDescription());
        service.addBook(two.getName(), two.getDescription());
    }

    @Test
    void addBookTest() {
        Book expected = new Book("математика", "для школы");
        service.addBook(expected.getName(), expected.getDescription());
        List<Book> books = service.getAllBooks();
        assertNotNull(books);
        Book actual = books.get(2);
        System.out.println();
        System.out.println(actual);
        System.out.println();
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void deleteBookByIdTest() {
        Book expected = new Book("книга 1", "описание");
//        service.deleteBookById(2);
        List<Book> books = service.getAllBooks();
        assertNotNull(books);
        Book actual = books.get(0);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void updateBookTest() {
        Book expected = new Book( "книга 2", "новое описание");
//        service.updateBook(expected.getId(), expected.getName(), expected.getDescription());
//        Book actual = service.getBookById(2);
//        assertNotNull(actual);
//        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void getBookByIdTest() {
        Book expected = new Book("книга 2", "описание");
//        Book actual = service.getBookById(2);
//        assertNotNull(actual);
//        assertEquals(expected.getName(), actual.getName());
//        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void getAllBooksTest() {
        Book one = new Book("книга 1", "описание");
        Book two = new Book("книга 2", "описание");
        List<Book> expected = new ArrayList<>();
        expected.add(one);
        expected.add(two);
        List<Book> actual = service.getAllBooks();
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void updateBookAuthors() {
        Book expected = new Book( "книга 1", "описание");
        Author one = new Author("Петя");
        Author two = new Author("Кинг");

        Set<Author> authors = new HashSet<>(Set.of(one, two));
        expected.setAuthors(authors);
//        service.updateBookAuthors(1, one.getName(), two.getName());
//        Book actual = service.getBookById(1);
//        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void updateBookGenres() {
        Book expected = new Book( "книга 1", "описание");
        Genre one = new Genre("жанр 1");
        Genre two = new Genre("жанр 2");

        Set<Genre> genres = new HashSet<>(Set.of(one, two));
        expected.setGenres(genres);
//        service.updateBookGenres(1, one.getName(), two.getName());
//        Book actual = service.getBookById(1);
//        assertEquals(expected.toString(), actual.toString());
    }
}