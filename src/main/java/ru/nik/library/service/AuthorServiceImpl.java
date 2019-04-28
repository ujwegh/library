package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Author;
import ru.nik.library.repository.AuthorDao;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorDao dao;

    @Autowired
    public AuthorServiceImpl(AuthorDao dao) {
        this.dao = dao;
    }

    @Override
    public Boolean addAuthor(String name) {
        Author author = new Author(null, name);
        return dao.insert(author) != 0;
    }

    @Override
    public Boolean deleteAuthorById(int id) {
        return dao.deleteById(id) != 0;
    }

    @Override
    public Boolean deleteAuthorByName(String name) {
        return dao.deleteByName(name) != 0;
    }

    @Override
    public Boolean updateAuthor(int id, String name) {
        Author author = new Author(id, name);
        return dao.update(author) != 0;
    }

    @Override
    public Author getAuthorByName(String name) {
        try {
            return dao.getByName(name);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Author getAuthorById(int id) {
        try {
            return dao.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Author> getAllAuthors() {
        return dao.getAll();
    }

    @Override
    public List<Author> getAllByNames(String... names) {
        return dao.getAllByNames(names);
    }
}
