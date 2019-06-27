package ru.nik.library.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.datajpa.GenreRepository;

import java.util.List;

@Service
@Transactional
public class GenreServiceImpl implements GenreService {
    private final GenreRepository repository;

    @Autowired
    public GenreServiceImpl(GenreRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean addGenre(String name) {
        Genre genre = new Genre(null, name);
        return repository.save(genre) != null;
    }

    @Override
    public Boolean deleteGenreById(int id) {
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
    public Boolean updateGenre(int id, String name) {
        Genre genre = new Genre(id, name);
        return repository.save(genre) != null;
    }

    @Override
    public Genre getGenreByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Genre getGenreById(int id) {
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
}
