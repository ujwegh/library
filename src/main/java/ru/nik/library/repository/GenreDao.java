package ru.nik.library.repository;

import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreDao {
    boolean insert(Genre genre);

    Genre update(Genre genre);

    Genre getById(int id);

    Genre getByName(String name);

    List<Genre> getAll();

    boolean deleteById(int id);

    boolean deleteByName(String name);

    List<Genre> getAllByNames(String... names);
}
