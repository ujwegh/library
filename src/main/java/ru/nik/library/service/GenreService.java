package ru.nik.library.service;

import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreService {
    Boolean addGenre(String name);

    Boolean deleteGenreById(int id);

    Boolean deleteGenreByName(String name);

    Boolean updateGenre(int id, String name);

    Genre getGenreByName(String name);

    Genre getGenreById(int id);

    List<Genre> getAllGenres();

    List<Genre> getAllByNames(String ... names);
}
