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
import java.util.stream.Collectors;

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
        return bookDao.update(new Book(id, name, description));
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
        Author author;
        Set<Author> authorSet = Arrays.stream(authors).map(Author::new).collect(Collectors.toSet());
        Book book = bookDao.getById(bookId);

        if (book != null) {
            List<Author> authorList = authorService.getAllByNames(authors);

            Set<Author> sett = new TreeSet<>(Comparator.comparing(Author::getName));

            sett.addAll(authorSet);
            sett.addAll(authorList);

            Set<Author> bookAuthors = book.getAuthors();
            bookAuthors.addAll(sett);
            book.setAuthors(bookAuthors);
            return bookDao.update(book);
        }
        return null;
    }

    @Override
    public Integer updateBookGenres(int bookId, String... genres) {
        Genre genre;
        Set<Genre> authorSet = Arrays.stream(genres).map(Genre::new).collect(Collectors.toSet());
        Book book = bookDao.getById(bookId);

        if (book != null) {
            List<Genre> genreList = genreService.getAllByNames(genres);

            Set<Genre> sett = new TreeSet<>(Comparator.comparing(Genre::getName));

            sett.addAll(authorSet);
            sett.addAll(genreList);

            Set<Genre> bookGenres = book.getGenres();
            bookGenres.addAll(sett);
            book.setGenres(bookGenres);
            return bookDao.update(book);
        }
        return null;
    }

}
