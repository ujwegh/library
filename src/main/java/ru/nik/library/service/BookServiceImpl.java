package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.datajpa.BookRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final AuthorService authorService;
    private final GenreService genreService;

    @Autowired
    public BookServiceImpl(BookRepository repository, AuthorService authorService, GenreService genreService) {
        this.repository = repository;
        this.authorService = authorService;
        this.genreService = genreService;
    }

    @Override
    public Boolean addBook(String name, String description) {
        return repository.save(new Book(name, description)) != null;
    }

    @Override
    public Boolean deleteBookById(int id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    @Override
    public Boolean updateBook(int id, String name, String description) {
        return repository.save(new Book(id, name, description)) != null;
    }

    @Override
    public Book getBookById(int id) {
        return repository.findById(id);
    }

    @Override
    public List<Book> getAllBooks() {
        return repository.findAll();
    }


    @Override
    public Boolean updateBookAuthors(int bookId, String... authors) {
        Set<Author> authorSet = Arrays.stream(authors).map(Author::new).collect(Collectors.toSet());
        Book book = repository.findById(bookId);

        Set<Author> toAdd = new HashSet<>();
        Set<Author> toDelete = new HashSet<>();
        if (book != null) {
            List<Author> authorList = authorService.getAllByNames(authors);
            authorSet.forEach(a -> authorList.stream()
                    .filter(dbAuthor -> a.getName().equals(dbAuthor.getName()))
                    .forEachOrdered(dbAuthor -> {
                        toDelete.add(a);
                        toAdd.add(dbAuthor);
                    }));
            authorSet.removeAll(toDelete);
            authorSet.addAll(toAdd);

            book.setAuthors(authorSet);
            return repository.save(book) != null;
        }
        return null;
    }

    @Override
    public Boolean updateBookGenres(int bookId, String... genres) {
        Set<Genre> genreSet = Arrays.stream(genres).map(Genre::new).collect(Collectors.toSet());
        Book book = repository.findById(bookId);

        Set<Genre> toAdd = new HashSet<>();
        Set<Genre> toDelete = new HashSet<>();
        if (book != null) {
            List<Genre> genreList = genreService.getAllByNames(genres);

            genreSet.forEach(a -> genreList.stream()
                    .filter(dbGenre -> a.getName().equals(dbGenre.getName()))
                    .forEachOrdered(dbGenre -> {
                        toDelete.add(a);
                        toAdd.add(dbGenre);
                    }));
            genreSet.removeAll(toDelete);
            genreSet.addAll(toAdd);

            book.setGenres(genreSet);
            return repository.save(book) != null;
        }
        return null;
    }

}
