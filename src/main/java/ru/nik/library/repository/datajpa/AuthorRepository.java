package ru.nik.library.repository.datajpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorRepository extends MongoRepository<Author, Integer> {

    List<Author> findAll();

    List<Author> findAllByNameIn(String... names);

    Author findByName(String name);

    Author findById(String id);

    void deleteById(String id) throws EmptyResultDataAccessException;

    int deleteByName(String name);
}
