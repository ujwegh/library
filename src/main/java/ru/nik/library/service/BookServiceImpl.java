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
        Book book = repository.findById(bookId);

        Set<Author> toAdd = new HashSet<>();
        Set<Author> toDelete = new HashSet<>();
        if (book != null) {
            Flux<Author> authorList = authorService.getAllByNames(authors);
            authorSet.forEach(a -> authorList.stream()
                    .filter(dbAuthor -> a.getName().equals(dbAuthor.getName()))
                    .forEachOrdered(dbAuthor -> {
                        toDelete.add(a);
                        toAdd.add(dbAuthor);
                    }));
            authorSet.removeAll(toDelete);
            authorSet.addAll(toAdd);

            List<Author> toSaveAuthors = new ArrayList<>(authorSet);
            authorService.saveAll(toSaveAuthors);
            book.setAuthors(authorSet);
            return repository.save(book) != null;
        }
        return null;
    }

    @Override
    public Boolean updateBookGenres(String bookId, String... genres) {
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

            List<Genre> toSaveAuthors = new ArrayList<>(genreSet);
            genreService.saveAll(toSaveAuthors);
            book.setGenres(genreSet);
            return repository.save(book) != null;
        }
        return null;
    }

}
