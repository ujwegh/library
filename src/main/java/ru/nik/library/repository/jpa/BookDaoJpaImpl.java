package ru.nik.library.repository.jpa;

import org.springframework.stereotype.Repository;
import ru.nik.library.domain.Book;
import ru.nik.library.repository.BookDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
@org.springframework.transaction.annotation.Transactional
public class BookDaoJpaImpl implements BookDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public int insert(Book book) {
        if (book.isNew()) {
            em.persist(book);
            return 1;
        } else {
            em.persist(book);
            return 2;
        }
    }

    @Override
    public Book getById(int id) {
        return em.find(Book.class, id);
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = em.createQuery("select a from Book a", Book.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public int deleteById(int id) {
        Query query = em.createQuery("delete from Book a where a.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }
}
