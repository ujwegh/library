package ru.nik.library.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@SqlGroup({
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql"),
        @Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
})
class BookServiceImplTest {

    @Autowired
    private BookService service;

    @Test
    void addBookTest() {
        Book expected = new Book("математика", "для школы",
                new Author("Перельман"), new Genre("задачник"));
        service.addBook(expected.getName(), expected.getDescription(),
                expected.getAuthor().getName(), expected.getGenre().getName());
        Book actual = service.getAllBooks().get(2);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void deleteBookByIdTest() {
        Book expected = new Book("сказки", "для взрослых",
                new Author("Кинг"), new Genre("детектив"));
        service.deleteBookById(1005);
        Book actual = service.getAllBooks().get(0);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void updateBookTest() {
        Book expected = new Book("сказки", "для взрослых",
                new Author("Пушкин"), new Genre("сказки"));
        service.updateBook(1004, expected.getName(), expected.getDescription(),
                expected.getAuthor().getName(), expected.getGenre().getName());
        Book actual = service.getBookById(1004);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void getBookByIdTest() {
        Book expected = new Book("сказки", "для взрослых",
                new Author("Кинг"), new Genre("детектив"));
        Book actual = service.getBookById(1004);
        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void getAllBooksTest() {
        List<Book> expected = new ArrayList<>();
        expected.add(new Book("сказки", "для взрослых",
                new Author("Кинг"), new Genre("детектив")));
        expected.add(new Book("сказки", "для детей",
                new Author("Пушкин"), new Genre("фантастика")));
        List<Book> actual = service.getAllBooks();
        assertEquals(expected.size(), actual.size());
    }
}