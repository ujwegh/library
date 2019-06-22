package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.GenreDao;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreDao dao;

    @Autowired
    public GenreServiceImpl(GenreDao dao) {
        this.dao = dao;
    }

    @Override
    public Integer addGenre(String name) {
        Genre genre = new Genre(null, name);
        return dao.insert(genre);
    }

    @Override
    public Integer deleteGenreById(int id) {
        return dao.deleteById(id);
    }

    @Override
    public Integer deleteGenreByName(String name) {
        return dao.deleteByName(name);
    }

    @Override
    public Integer updateGenre(int id, String name) {
        Genre genre = new Genre(id, name);
        return dao.insert(genre);
    }

    @Override
    public Genre getGenreByName(String name) {
        try {
            return dao.getByName(name);
        } catch (DataAccessException e) {
            return null;
        }

    }

    @Override
    public Genre getGenreById(int id) {
        try {
            return dao.getById(id);
        } catch (DataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Genre> getAllGenres() {
        return dao.getAll();
    }
}
