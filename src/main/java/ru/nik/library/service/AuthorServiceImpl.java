package ru.nik.library.service;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Author;
import ru.nik.library.repository.datajpa.AuthorRepository;

import java.util.List;

@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean addAuthor(String name) {
        Author author = new Author(null, name);
        return repository.save(author) != null;
    }

    @Override
    public Boolean deleteAuthorById(int id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Boolean deleteAuthorByName(String name) {
        return repository.deleteByName(name) != 0;
    }

    @Override
    public Boolean updateAuthor(int id, String name) {
        Author author = new Author(id, name);
        return repository.save(author) != null;
    }

    @Override
    public Author getAuthorByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Author getAuthorById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Author> getAllAuthors() {
        return repository.findAll();
    }

    @Override
    public List<Author> getAllByNames(String... names) {
        return repository.findAllByNameIn(names);
    }
}
