package ru.nik.library.repository.jpa;

import org.springframework.stereotype.Repository;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.GenreDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class GenreDaoJpaImpl implements GenreDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public boolean insert(Genre genre) {
        try {
            em.persist(genre);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public Genre update(Genre genre) {
        return em.merge(genre);
    }

    @Override
    public Genre getById(int id) {
        return em.find(Genre.class, id);
    }

    @Override
    public Genre getByName(String name) {
        TypedQuery<Genre> query = em.createQuery("select a from Genre a where a.name=:name", Genre.class);
        query.setParameter("name", name);
        return query.getSingleResult();
    }

    @Override
    public List<Genre> getAll() {
        TypedQuery<Genre> query = em.createQuery("select a from Genre a", Genre.class);
        return query.getResultList();
    }

    @Override
    @Transactional
    public boolean deleteById(int id) {
        Query query = em.createQuery("delete from Genre a where a.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate() != 0;
    }

    @Override
    @Transactional
    public boolean deleteByName(String name) {
        Query query = em.createQuery("delete from Genre a where a.name = :name");
        query.setParameter("name", name);
        return query.executeUpdate() != 0;
    }

    @Override
    public List<Genre> getAllByNames(String... names) {
        List<String> nameList = Arrays.asList(names);
        TypedQuery<Genre> query = em.createQuery("select a from Genre a where a.name in (:names)", Genre.class);
        query.setParameter("names", nameList);
        return query.getResultList();
    }
}
