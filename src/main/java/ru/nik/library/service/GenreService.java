package ru.nik.library.service;

import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreService {
    Boolean addGenre(String name);

    Boolean deleteGenreById(String id);

    Boolean deleteGenreByName(String name);

    Boolean updateGenre(String id, String name);

    Genre getGenreByName(String name);

    Genre getGenreById(String id);

    List<Genre> getAllGenres();

    List<Genre> getAllByNames(String ... names);

    List<Genre> saveAll(List<Genre> genres);
}
