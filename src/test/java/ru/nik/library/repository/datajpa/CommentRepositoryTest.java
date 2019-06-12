package ru.nik.library.repository.datajpa;

//import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataMongoTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = {CommentRepository.class, BookRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;
    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    public void init() {
        Book book = new Book("aaa", "книга", "интересная");
        Book book2 = new Book("bbb", "книга 2", "можно почитать");
        bookRepository.save(book);
        bookRepository.save(book2);
        Comment one = new Comment("aa", "коммент 1");
        one.setBook(book);
        Comment two = new Comment("bb", "коммент 2");
        two.setBook(book);
        Comment three = new Comment("cc", "коммент 1");
        three.setBook(book2);
        repository.save(one);
        repository.save(two);
        repository.save(three);
    }

    @Test
    void save() {
        Book book = bookRepository.findById("aaa");
        Comment comment = new Comment("dd","какой-то коммент");
        comment.setBook(book);
        Comment actual = repository.save(comment);
        assertNotNull(actual);
        assertEquals(comment.toString(), actual.toString());
    }


    @Test
    void findAllByBook_Id() {
        List<Comment> comments = repository.findAllByBook_Id("aaa");
        assertNotNull(comments);
        assertEquals(2, comments.size());
    }

    @Test
    void findByIdAndBook_Id() {
        Comment comment = new Comment("bb","коммент 2");
        Comment actual = repository.findByIdAndBook_Id("bb", "aaa");
        assertNotNull(actual);
        assertEquals(comment.getId(), actual.getId());
        assertEquals(comment.getComment(), actual.getComment());
    }

    @Test
    void deleteByIdAndBook_Id() {
        Comment comment = new Comment("bb","коммент 2");
        repository.deleteByIdAndBook_Id("bb", "aaa");
        List<Comment> comments = repository.findAllByBook_Id("aaa");
        assertNotNull(comments);
        assertEquals(1,comments.size());
        assertNull(repository.findByIdAndBook_Id("bb","aaa"));
    }
}