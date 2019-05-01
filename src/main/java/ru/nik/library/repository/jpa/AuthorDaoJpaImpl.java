package ru.nik.library.repository.jpa;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.nik.library.domain.Author;
import ru.nik.library.repository.AuthorDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class AuthorDaoJpaImpl implements AuthorDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public boolean insert(Author author) {
        try {
            em.persist(author);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Author update(Author author) {
        return em.merge(author);
    }

    @Override
    public Author getById(int id) {
        return em.find(Author.class, id);
    }

    @Override
    public Author getByName(String name) {
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name = :name", Author.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public List<Author> getAll() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public boolean deleteById(int id) {
        Query query = em.createQuery("delete from Author a where a.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate() != 0;
    }

    @Override
    @Transactional
    public boolean deleteByName(String name) {
        Query query = em.createQuery("delete from Author a where a.name = :name");
        query.setParameter("name", name);
        return query.executeUpdate() != 0;
    }

    @Override
    public List<Author> getAllByNames(String... names) {
        List<String> nameList = Arrays.asList(names);
        TypedQuery<Author> query = em.createQuery("select a from Author a where a.name in (:names)", Author.class);
        query.setParameter("names", nameList);
        return query.getResultList();
    }
}
