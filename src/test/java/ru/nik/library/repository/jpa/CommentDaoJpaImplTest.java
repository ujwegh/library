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
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.repository.BookDao;
import ru.nik.library.repository.CommentDao;

import javax.persistence.NoResultException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@PropertySource("classpath:application-test.properties")
@ContextConfiguration(classes = {CommentDaoJpaImpl.class, BookDaoJpaImpl.class})
@EntityScan(basePackages = "ru.nik.library.domain")
@EnableAutoConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CommentDaoJpaImplTest {

    @Autowired
    private CommentDao dao;

    @Autowired
    private BookDao bookDao;

    @BeforeEach
    public void init() {
        bookDao.insert(new Book("автор", "описание"));
        Comment one = new Comment("коммент 1");
        Comment two = new Comment("коммент 2");
        dao.insert(one, 1);
        dao.insert(two, 1);
    }

    @Test
    void insert() {
        Comment comment = new Comment("плохой учебник, не читайте");
        int i = dao.insert(comment, 1);
        List<Comment> comments = dao.getAll(1);
        assertNotNull(comment);
        assertEquals(1, i);
        assertEquals(3, comments.size());
    }

    @Test
    void update() {
        Comment expected = dao.getById(1, 1);
        expected.setComment("был коммент 1, а стал коммент 3");
        int i = dao.update(expected, expected.getBook().getId());
        assertEquals(2, i);
        Comment actual = dao.getById(1, 1);
        assertNotNull(actual);
        assertEquals(expected, actual);

    }

    @Test
    void getById() {
        Comment expected = new Comment("коммент 1");
        Comment actual = dao.getById(1, 1);
        assertNotNull(expected);
        assertEquals(expected.getComment(), actual.getComment());
    }

    @Test
    void getAll() {
        List<Comment> comments = dao.getAll(1);
        assertNotNull(comments);
        assertEquals(2, comments.size());
    }

    @Test
    void deleteById() {
        int i = dao.deleteById(1, 1);
        assertEquals(1, i);
        assertThrows(NoResultException.class, () -> dao.getById(1, 1));
    }
}