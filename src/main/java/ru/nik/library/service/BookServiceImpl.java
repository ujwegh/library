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

import java.util.*;

@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Autowired
    public BookServiceImpl(BookDao dao, AuthorService authorService, GenreService genreService) {
        this.bookDao = dao;
        this.authorService = authorService;
        this.genreService = genreService;
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
        List<Author> authorList = new ArrayList<>();
        Book book = bookDao.getById(bookId);
        if (book != null) {
            for (String name : authors) {
                try {
                    author = authorService.getAuthorByName(name);
                } catch (Exception ignored) {}

                if (author == null) {
                    author = new Author(null, name);
                    author.setBooks(Set.of(book));
//                    authorService.addAuthor();
                } else {
                    author.setBooks(Set.of(book));

                }

                authorList.add(author);
            }
        }

        Set<Author> bookAuthors = book.getAuthors();
        bookAuthors.add(author);
        book.setAuthors(bookAuthors);
        return bookDao.insert(book);
    }

    @Override
    public Integer updateBookGenres(int bookId, String... genres) {
        Author author = null;
        Genre genre = null;

//        try {
//            author = authorService.getAuthorByName(authorName);
//        } catch (Exception ignored) {
//        }
//        try {
//            genre = genreService.getGenreByName(genreName);
//        } catch (Exception ignored) {
//        }
//
//        if (author == null) {
//            author = new Author(null, authorName);
//            authorService.addAuthor(authorName);
//        }
//        if (genre == null) {
//            genre = new Genre(null, genreName);
//            genreService.addGenre(genreName);
//        }
//
//        Book book = bookDao.getById(id);
//        Set<Author> authors = book.getAuthors();
//        authors.add(author);
//        Set<Genre> genres = book.getGenres();
//        genres.add(genre);
        return null;
    }

}
