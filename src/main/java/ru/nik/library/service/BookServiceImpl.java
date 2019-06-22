package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.AuthorDao;
import ru.nik.library.repository.BookDao;
import ru.nik.library.repository.GenreDao;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Autowired
    public BookServiceImpl(BookDao dao, AuthorDao authorDao, GenreDao genreDao) {
        this.bookDao = dao;
        this.authorDao = authorDao;
        this.genreDao = genreDao;
    }

    @Override
    public Integer addBook(String name, String description, String authorName, String genreName) {
        Author author = null;
        Genre genre = null;

        try {
            author = authorDao.getByName(authorName);
        } catch (Exception ignored){}
        try {
            genre = genreDao.getByName(genreName);
        } catch (Exception ignored) {}

        if (author == null) {
            author = new Author(null, authorName);
            authorDao.insert(author);
        }
        if (genre == null) {
            genre = new Genre(null, genreName);
            genreDao.insert(genre);
        }

        Book book = new Book(name, description, author, genre);
        return bookDao.insert(book);
    }

    @Override
    public Integer deleteBookById(int id) {
        return bookDao.deleteById(id);
    }

    @Override
    public Integer updateBook(int id,String name, String description, String authorName, String genreName) {
        Author author = null;
        Genre genre = null;

        try {
            author = authorDao.getByName(authorName);
        } catch (Exception ignored){}
        try {
            genre = genreDao.getByName(genreName);
        } catch (Exception ignored) {}

        if (author == null) {
            author = new Author(null, authorName);
            authorDao.insert(author);
        }
        if (genre == null) {
            genre = new Genre(null, genreName);
            genreDao.insert(genre);
        }

        Book book = new Book(id, name, description, author, genre);
        return bookDao.insert(book);
    }

    @Override
    public Book getBookById(int id) {
        try {
            return bookDao.getById(id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }

}
