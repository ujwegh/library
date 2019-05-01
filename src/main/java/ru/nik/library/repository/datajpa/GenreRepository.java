package ru.nik.library.repository.datajpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;
import ru.nik.library.domain.Genre;

import javax.transaction.Transactional;
import java.util.List;

public interface GenreRepository extends CrudRepository<Genre, Integer> {
    List<Genre> findAll();

    List<Genre> findAllByNameIn(String... names);

    Genre findByName(String name);

    Genre findById(int id);

    void deleteById(int id) throws EmptyResultDataAccessException;

    @Transactional
    int deleteByName(String name);
}
