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
        int i = dao.insert(book);
        List<Book> books = dao.getAll();
//        books.forEach(b-> System.out.println(b));
        assertEquals(1, i);
        assertEquals(3, books.size());
    }

    @Test
    void update() {
        Book expected = dao.getById(1);
        expected.setComments(List.of(new Comment("ляляля")));
        expected.setAuthors(Set.of(new Author("новый автор")));
        expected.setGenres(Set.of(new Genre("бульварное чтиво")));
        int i = dao.insert(expected);
        Book actual = dao.getById(1);
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
        int i = dao.deleteById(1);
        List<Book> books = dao.getAll();
        assertEquals(1, i);
        assertEquals(1,books.size());
    }
}