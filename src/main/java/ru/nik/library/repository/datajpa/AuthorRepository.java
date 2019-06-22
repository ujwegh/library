package ru.nik.library.repository.datajpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;
import ru.nik.library.domain.Author;

import javax.transaction.Transactional;
import java.util.List;

public interface AuthorRepository extends CrudRepository<Author, Integer> {

    List<Author> findAll();

    List<Author> findAllByNameIn(String... names);

    Author findByName(String name);

    Author findById(int id);

    void deleteById(int id) throws EmptyResultDataAccessException;

    @Transactional
    int deleteByName(String name);
}
