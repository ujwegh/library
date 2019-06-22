package ru.nik.library.repository;

import org.springframework.dao.DataAccessException;
import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreDao {
    int insert(Genre genre);

    Genre getById(int n) throws DataAccessException;

    Genre getByName(String name) throws DataAccessException;

    List<Genre> getAll();

    int deleteById(int id);

    int deleteByName(String name);
}
