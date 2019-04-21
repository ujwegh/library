package ru.nik.library.repository.jpa;

import org.springframework.stereotype.Repository;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.GenreDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@SuppressWarnings("JpaQlInspection")
@Repository
public class GenreDaoJpaImpl implements GenreDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public int insert(Genre genre) {
        if (genre.isNew()) {
            em.persist(genre);
            return 1;
        } else {
            em.merge(genre);
            return 2;
        }
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
    public int deleteById(int id) {
        Query query = em.createQuery("delete from Genre a where a.id = :id");
        query.setParameter("id", id);
        return query.executeUpdate();
    }

    @Override
    @Transactional
    public int deleteByName(String name) {
        Query query = em.createQuery("delete from Genre a where a.name = :name");
        query.setParameter("name", name);
        return query.executeUpdate();
    }
}
