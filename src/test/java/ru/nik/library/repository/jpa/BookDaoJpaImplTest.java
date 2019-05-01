package ru.nik.library.repository.jpa;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.BookDao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = BookDaoJpaImpl.class)
@EntityScan(basePackages = "ru.nik.library.domain")
@EnableAutoConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BookDaoJpaImplTest {

    @Autowired
    private BookDao dao;

    @BeforeEach
    public void init() {
        Book one = new Book("очень старая книжка", "непонятная");
        Book two = new Book("старая книжка", "занятная");
        dao.insert(one);
        dao.insert(two);
    }

    @Test
    void insert() {
        Book book = new Book("новая книжка", "очень интересная");
        book.setAuthors(Set.of(new Author("Пушкин"), new Author("Лермонтов")));
        book.setComments(List.of(new Comment("вот это хренотаа"), new Comment("советую почитать")));
        boolean b = dao.insert(book);
        List<Book> books = dao.getAll();
        assertTrue(b);
        assertEquals(3, books.size());
    }

    @Test
    void update() {
        Book expected = dao.getById(1);
        List<Comment> list = new ArrayList<>();
        list.add(new Comment("ddd"));
        Set<Author> authors = new HashSet<>();
        authors.add(new Author("новый автор"));
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre("бульварное чтиво"));

        expected.setComments(list);
        expected.setAuthors(authors);
        expected.setGenres(genres);
        Book book = dao.update(expected);
        assertNotNull(book);
        Book actual = dao.getById(1);
        System.out.println(actual);
        assertNotNull(actual);
        assertEquals(expected, actual);
    }

    @Test
    void getById() {
        Book expected = new Book("очень старая книжка", "непонятная");
        Book actual = dao.getById(1);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
    }

    @Test
    void getAll() {
        Book one = new Book("очень старая книжка", "непонятная");
        Book two = new Book("старая книжка", "занятная");
        List<Book> expected = new ArrayList<>();
        expected.add(one);
        expected.add(two);

        List<Book> actual = dao.getAll();
        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void deleteById() {
        boolean b = dao.deleteById(1);
        assertTrue(b);
        List<Book> books = dao.getAll();
        assertEquals(1, books.size());
    }
}