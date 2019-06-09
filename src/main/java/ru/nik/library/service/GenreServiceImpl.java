package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.datajpa.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;

    @Autowired
    public GenreServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean addGenre(String name) {
        Genre genre = new Genre( name);
        return repository.save(genre) != null;
    }

    @Override
    public Boolean deleteGenreById(String id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Boolean deleteGenreByName(String name) {
        return repository.deleteByName(name) != 0;
    }

    @Override
    public Boolean updateGenre(String id, String name) {
        Genre genre = repository.findById(id);
        genre.setName(name);
        return repository.save(genre) != null;
    }

    @Override
    public Genre getGenreByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Genre getGenreById(String id) {
        return repository.findById(id);
    }

    @Override
    public List<Genre> getAllGenres() {
        return repository.findAll();
    }

    @Override
    public List<Genre> getAllByNames(String... names) {
        return repository.findAllByNameIn(names);
    }

    @Override
    public List<Genre> saveAll(List<Genre> genres) {
        return repository.saveAll(genres);
    }
}
