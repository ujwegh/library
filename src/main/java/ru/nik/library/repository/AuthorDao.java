package ru.nik.library.repository;

import org.springframework.dao.DataAccessException;
import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    int insert(Author author);

    Author getById(int n) throws DataAccessException;

    Author getByName(String name) throws DataAccessException;

    List<Author> getAll();

    int deleteById(int id);

    int deleteByName(String name);
}
