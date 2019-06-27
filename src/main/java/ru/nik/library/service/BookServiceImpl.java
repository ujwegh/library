package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    public Mono<Book> addBook(String name, String description) {
        return repository.save(new Book(name, description));
    }

    @Override
    public Mono<Boolean> deleteBookById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Book> updateBook(String id, String name, String description) {
        return repository.findById(id).doOnSuccess(book -> {
            book.setName(name);
            book.setDescription(description);
            repository.save(book);
        });
    }

    @Override
    public Mono<Book> getBookById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Book> getAllBooks() {
        return repository.findAll();
    }


    @Override
    public Mono<Book> updateBookAuthors(String bookId, String... authors) {
        Set<Author> authorSet = Arrays.stream(authors).map(Author::new).collect(Collectors.toSet());
        Mono<Book> book = repository.findById(bookId);

        Set<Author> toAdd = new HashSet<>();
        Set<Author> toDelete = new HashSet<>();
        if (book != null) {
            authorSet.forEach(a -> authorService.getAllByNames(authors).map(author -> {
                if (a.getName().equals(author.getName())) {
                    toDelete.add(a);
                    toAdd.add(author);
                }
                return author;
            }));
            authorSet.removeAll(toDelete);
            authorSet.addAll(toAdd);
            List<Author> toSaveAuthors = new ArrayList<>(authorSet);

            authorService.saveAll(toSaveAuthors);
            return book.doOnSuccess(b -> b.setAuthors(authorSet)).doOnSuccess(repository::save);
        }
        return null;
    }

    @Override
    public Mono<Book> updateBookGenres(String bookId, String... genres) {
        Set<Genre> genreSet = Arrays.stream(genres).map(Genre::new).collect(Collectors.toSet());
        Mono<Book> book = repository.findById(bookId);

        Set<Genre> toAdd = new HashSet<>();
        Set<Genre> toDelete = new HashSet<>();
        if (book != null) {
            genreSet.forEach(a -> genreService.getAllByNames(genres).map(genre -> {
                if (a.getName().equals(genre.getName())) {
                    toDelete.add(a);
                    toAdd.add(genre);
                }
                return genre;
            }));
            genreSet.removeAll(toDelete);
            genreSet.addAll(toAdd);

            List<Genre> toSaveAuthors = new ArrayList<>(genreSet);

            genreService.saveAll(toSaveAuthors);
            return book.doOnSuccess(b -> b.setGenres(genreSet)).doOnSuccess(repository::save);
        }
        return null;
    }

}
