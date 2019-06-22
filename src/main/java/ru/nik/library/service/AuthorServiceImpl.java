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
    public Integer addAuthor(String name) {
        Author author = new Author(null, name);
        return dao.insert(author);
    }

    @Override
    public Integer deleteAuthorById(int id) {
        return dao.deleteById(id);
    }

    @Override
    public Integer deleteAuthorByName(String name) {
        return dao.deleteByName(name);
    }

    @Override
    public Integer updateAuthor(int id, String name) {
        Author author = new Author(id, name);
        return dao.insert(author);
    }

    @Override
    public Author getAuthorByName(String name) {
        try {
            return dao.getByName(name);
        } catch (DataAccessException e) {
            return null;
        }

    }

    @Override
    public Author getAuthorById(int id) {
        try {
            return dao.getById(id);
        } catch (DataAccessException e) {
            return null;
        }

    }

    @Override
    public List<Author> getAllAutors() {
        return dao.getAll();
    }
}
