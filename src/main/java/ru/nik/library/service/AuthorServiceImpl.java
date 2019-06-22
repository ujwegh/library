package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Author;
import ru.nik.library.repository.datajpa.AuthorRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository repository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Boolean addAuthor(String name) {
        Author author = new Author(name);
        return repository.save(author) != null;
    }

    @Override
    public Boolean deleteAuthorById(String id) {
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
    public Boolean updateAuthor(String id, String name) {
        Author author = repository.findById(id);
        author.setName(name);
        return repository.save(author) != null;
    }

    @Override
    public Author getAuthorByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Author getAuthorById(String id) {
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

    @Override
    public List<Author> saveAll(List<Author> authors) {
        return repository.saveAll(authors);
    }
}
