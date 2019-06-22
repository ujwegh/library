package ru.nik.library.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.repository.CommentDao;

import javax.persistence.*;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class CommentDaoJpaImpl implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public int insert(Comment comment, int bookId) {
        comment.setBook(em.getReference(Book.class, bookId));
        try {
            em.persist(comment);
            return 1;
        } catch (EntityExistsException e) {
            return 0;
        }
    }

    @Override
    @Transactional
    public int update(Comment comment, int bookId) {
        comment.setBook(em.getReference(Book.class, bookId));
        em.merge(comment);
        return 2;
    }

    @Override
    public Comment getById(int id, int bookId) {
        TypedQuery<Comment> query =
                em.createQuery("select c from Comment c where c.id=:id and c.book.id=:bookId",
                        Comment.class);
        query.setParameter("id", id);
        query.setParameter("bookId", bookId);
        return query.getSingleResult();
    }

    @Override
    public List<Comment> getAll(int bookId) {
        TypedQuery<Comment> query =
                em.createQuery("select c from Comment c where c.book.id=:bookId", Comment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    @Transactional
    public int deleteById(int id, int bookId) {
        Query query = em.createQuery("delete from Comment a where a.id = :id and a.book.id=:bookId");
        query.setParameter("id", id);
        query.setParameter("bookId", bookId);
        return query.executeUpdate();
    }
}
