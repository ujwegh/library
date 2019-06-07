package ru.nik.library.repository.datajpa;

import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
@DataJpaTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = {CommentRepository.class, BookRepository.class})
@EnableAutoConfiguration
@EntityScan(basePackages = "ru.nik.library.domain")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentRepositoryTest {

    @Autowired
    private CommentRepository repository;
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EntityManager manager;

    @BeforeEach
    public void init() {
        Book book = new Book("книга", "интересная");
        Book book2 = new Book("книга 2", "можно почитать");
        manager.persist(book);
        manager.persist(book2);
        Comment one = new Comment("коммент 1");
        one.setBook(book);
        Comment two = new Comment("коммент 2");
        two.setBook(book);
        Comment three = new Comment("коммент 1");
        three.setBook(book2);
        manager.persist(one);
        manager.persist(two);
        manager.persist(three);
    }

    @Test
    void save() {
        Book book = bookRepository.findById(1);
        Comment comment = new Comment("какой-то коммент");
        comment.setBook(book);
        Comment actual = repository.save(comment);
        comment.setId(3);
        assertNotNull(actual);
        assertEquals(comment.toString(), actual.toString());
    }


    @Test
    void findAllByBook_Id() {
        List<Comment> comments = repository.findAllByBook_Id(2);
        System.out.println(comments);
    }

    @Test
    void findByIdAndBook_Id() {
        Comment comment = new Comment(2,"коммент 2");
        Comment actual = repository.findByIdAndBook_Id(2, 1);
        assertNotNull(actual);
        assertEquals(comment.getId(), actual.getId());
        assertEquals(comment.getComment(), actual.getComment());
    }

    @Test
    void deleteByIdAndBook_Id() {
        Comment comment = new Comment(2,"коммент 2");
        repository.deleteByIdAndBook_Id(1, 1);
        List<Comment> comments = repository.findAllByBook_Id(1);
        assertNotNull(comments);
        assertEquals(1,comments.size());
        assertNull(repository.findByIdAndBook_Id(1,1));
    }
}