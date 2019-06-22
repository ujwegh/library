package ru.nik.library.repository;

import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreDao {
    int insert(Genre genre);

    int update(Genre genre);

    Genre getById(int id);

    Genre getByName(String name);

    List<Genre> getAll();

    int deleteById(int id);

    int deleteByName(String name);

    List<Genre> getAllByNames(String... names);
}
