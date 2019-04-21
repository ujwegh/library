package ru.nik.library.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
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

    @Autowired
    private AuthorService authorService;

    @BeforeEach
    void init() {
        Book one = new Book("книга 1", "описание");
        Book two = new Book("книга 2", "описание");
        service.addBook(one.getName(), one.getDescription());
        service.addBook(two.getName(), two.getDescription());
        authorService.addAuthor("вова");
    }

    @Test
    void addBookTest() {
        Book expected = new Book("математика", "для школы");
        System.out.println(authorService.getAllAutors());
        expected.setAuthors(Set.of());
        service.addBook(expected.getName(), expected.getDescription());
        List<Book> books = service.getAllBooks();
        assertNotNull(books);
        Book actual = books.get(0);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }
//
//    @Test
//    void deleteBookByIdTest() {
//        Book expected = new Book("сказки", "для взрослых",
//                new Author("Кинг"), new Genre("детектив"));
//        service.deleteBookById(1005);
//        Book actual = service.getAllBooks().get(0);
//        assertEquals(expected.toString(), actual.toString());
//    }
//
//    @Test
//    void updateBookTest() {
//        Book expected = new Book("сказки", "для взрослых",
//                new Author("Пушкин"), new Genre("сказки"));
//        service.updateBook(1004, expected.getName(), expected.getDescription(),
//                expected.getAuthor().getName(), expected.getGenre().getName());
//        Book actual = service.getBookById(1004);
//        assertEquals(expected.toString(), actual.toString());
//    }
//
//    @Test
//    void getBookByIdTest() {
//        Book expected = new Book("сказки", "для взрослых",
//                new Author("Кинг"), new Genre("детектив"));
//        Book actual = service.getBookById(1004);
//        assertEquals(expected.toString(), actual.toString());
//    }
//
//    @Test
//    void getAllBooksTest() {
//        List<Book> expected = new ArrayList<>();
//        expected.add(new Book("сказки", "для взрослых",
//                new Author("Кинг"), new Genre("детектив")));
//        expected.add(new Book("сказки", "для детей",
//                new Author("Пушкин"), new Genre("фантастика")));
//        List<Book> actual = service.getAllBooks();
//        assertEquals(expected.size(), actual.size());
//    }
}