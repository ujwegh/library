package ru.nik.library.repository.datajpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreRepository extends MongoRepository<Genre, Integer> {
    List<Genre> findAll();

    List<Genre> findAllByNameIn(String... names);

    Genre findByName(String name);

    Genre findById(String id);

    void deleteById(String id) throws EmptyResultDataAccessException;

    int deleteByName(String name);
}
