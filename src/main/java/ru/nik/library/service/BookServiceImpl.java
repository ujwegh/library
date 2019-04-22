package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.AuthorDao;
import ru.nik.library.repository.BookDao;
import ru.nik.library.repository.GenreDao;

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorDao authorDao;
    private final GenreDao genreDao;

    @Autowired
    public BookServiceImpl(BookDao dao, AuthorDao dao1, GenreDao genreDao) {
        this.bookDao = dao;
        this.authorDao = dao1;
        this.genreDao = genreDao;
    }

    @Override
    public Integer addBook(String name, String description) {
        return bookDao.insert(new Book(name, description));
    }

    @Override
    public Integer deleteBookById(int id) {
        return bookDao.deleteById(id);
    }

    @Override
    public Integer updateBook(int id, String name, String description) {
        return bookDao.insert(new Book(id, name, description));
    }

    @Override
    public Book getBookById(int id) {
        try {
            return bookDao.getById(id);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookDao.getAll();
    }


    @Override
    public Integer updateBookAuthors(int bookId, String... authors) {
        Author author = null;
        Set<Author> authorSet = new HashSet<>();
        Book book = bookDao.getById(bookId);
        if (book != null) {
            for (String name : authors) {
                try {
                    author = authorDao.getByName(name);
                } catch (Exception ignored) {}

                if (author == null) {
                    author = new Author(null, name);
                    author.setBooks(Set.of(book));
                    authorDao.insert(author);
                } else {
                    author.setBooks(Set.of(book));
                }
                authorSet.add(author);
            }
            Set<Author> bookAuthors = book.getAuthors();
            bookAuthors.addAll(authorSet);
            book.setAuthors(bookAuthors);
            return bookDao.insert(book);
        }
        return null;
    }

    @Override
    public Integer updateBookGenres(int bookId, String... genres) {
        Genre genre = null;
        Set<Genre> genreSet = new HashSet<>();
        Book book = bookDao.getById(bookId);
        if (book != null) {
            for (String name : genres) {
                try {
                    genre = genreDao.getByName(name);
                } catch (Exception ignored) {}

                if (genre == null) {
                    genre = new Genre(null, name);
                    genre.setBooks(Set.of(book));
                    genreDao.insert(genre);
                } else {
                    genre.setBooks(Set.of(book));
                }
                genreSet.add(genre);
            }
            Set<Genre> bookAuthors = book.getGenres();
            bookAuthors.addAll(genreSet);
            book.setGenres(bookAuthors);
            return bookDao.insert(book);
        }
        return null;
    }

}
