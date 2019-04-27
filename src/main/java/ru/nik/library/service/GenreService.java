package ru.nik.library.service;

import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreService {
    Integer addGenre(String name);

    Integer deleteGenreById(int id);

    Integer deleteGenreByName(String name);

    Integer updateGenre(int id, String name);

    Genre getGenreByName(String name);

    Genre getGenreById(int id);

    List<Genre> getAllGenres();

    List<Genre> getAllByNames(String ... names);
}
