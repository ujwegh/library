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
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
    ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT_ENABLED + "=false"
})
@EnableMongoRepositories(basePackages = {"ru.nik.library.repository"})
@EnableAutoConfiguration
@ContextConfiguration(classes = {BookServiceImpl.class, AuthorServiceImpl.class,
    GenreServiceImpl.class})
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

//    @Test
//    void addBookTest() {
//        Book expected = new Book("математика", "для школы");
//        service.addBook(expected.getName(), expected.getDescription());
//        List<Book> books = service.getAllBooks();
//        assertNotNull(books);
//        Book actual = books.get(2);
//        System.out.println();
//        System.out.println(actual);
//        System.out.println();
//        assertEquals(expected.getName(), actual.getName());
//        assertEquals(expected.getDescription(), actual.getDescription());
//    }
//
//    @Test
//    void deleteBookByIdTest() {
//        List<Book> allBooks = service.getAllBooks();
//        Book expected = allBooks.get(0);
//        service.deleteBookById(expected.getId());
//        List<Book> books = service.getAllBooks();
//        System.out.println(books);
//        assertNotNull(books);
//        Book actual = books.get(0);
//        assertEquals("книга 2", actual.getName());
//        assertEquals("описание", actual.getDescription());
//    }
//
//    @Test
//    void updateBookTest() {
//        List<Book> allBooks = service.getAllBooks();
//        Book expected = allBooks.get(0);
//        expected.setName("новый автор");
//        expected.setDescription("хорошая книжка");
//        service.updateBook(expected.getId(), expected.getName(), expected.getDescription());
//        Book actual = service.getBookById(expected.getId());
//        assertNotNull(actual);
//        assertEquals(expected.toString(), actual.toString());
//    }
//
//    @Test
//    void getBookByIdTest() {
//        List<Book> allBooks = service.getAllBooks();
//        Book expected = allBooks.get(0);
//        Book actual = service.getBookById(expected.getId());
//        assertNotNull(actual);
//        assertEquals(expected.getName(), actual.getName());
//        assertEquals(expected.getDescription(), actual.getDescription());
//    }
//
//    @Test
//    void getAllBooksTest() {
//        Book one = new Book("книга 1", "описание");
//        Book two = new Book("книга 2", "описание");
//        List<Book> expected = new ArrayList<>();
//        expected.add(one);
//        expected.add(two);
//        List<Book> actual = service.getAllBooks();
//        assertEquals(expected.size(), actual.size());
//    }
//
//    @Test
//    void updateBookAuthors() {
//        List<Book> allBooks = service.getAllBooks();
//        Book expected = allBooks.get(0);
//        Author one = new Author("Петя");
//        Author two = new Author("Кинг");
//
//        Set<Author> authors = new HashSet<>(Set.of(one, two));
//        expected.setAuthors(authors);
//        service.updateBookAuthors(expected.getId(), one.getName(), two.getName());
//        Book actual = service.getBookById(expected.getId());
//        assertEquals(expected.toString(), actual.toString());
//    }
//
//    @Test
//    void updateBookGenres() {
//        List<Book> allBooks = service.getAllBooks();
//        Book expected = allBooks.get(0);
//        Genre one = new Genre("жанр 1");
//        Genre two = new Genre("жанр 2");
//
//        Set<Genre> genres = new HashSet<>(Set.of(one, two));
//        expected.setGenres(genres);
//        service.updateBookGenres(expected.getId(), one.getName(), two.getName());
//        Book actual = service.getBookById(expected.getId());
//        assertEquals(expected.toString(), actual.toString());
//    }
}