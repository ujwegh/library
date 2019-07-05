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
    public Mono<Void> deleteBookById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Book> updateBook(String id, String name, String description) {
		Mono<Book> bookMono = repository.findById(id).map(book -> {
			book.setName(name);
			book.setDescription(description);
			return book;
		});

		return repository.save(bookMono.block());
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
            List<Author> existedAuthors = authorService.getAllByNames(authors).collectList().block();

            authorSet.forEach(author -> existedAuthors.forEach(existedAuthor -> {
					if (author.getName().equals(existedAuthor.getName())) {
						toDelete.add(author);
						toAdd.add(existedAuthor);
					}
				})
			);

            authorSet.removeAll(toDelete);
            authorSet.addAll(toAdd);
            List<Author> toSaveAuthors = new ArrayList<>(authorSet);

			toSaveAuthors.forEach(System.out::println);
            Flux<Author> authorFlux = authorService.saveAll(toSaveAuthors);
            Set<Author> set = new HashSet<>(
				Objects.requireNonNull(authorFlux.collectList().block()));
            Mono<Book> mono = book.map(b -> {
            	b.setAuthors(set);
            	return b;
			});
            return repository.save(mono.block());
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
			List<Genre> existedGenres = genreService.getAllByNames(genres).collectList().block();

			genreSet.forEach(genre -> existedGenres.forEach(existedGenre -> {
					if (genre.getName().equals(existedGenre.getName())) {
						toDelete.add(genre);
						toAdd.add(existedGenre);
					}
				})
			);

            genreSet.removeAll(toDelete);
            genreSet.addAll(toAdd);

            List<Genre> toSaveGenres = new ArrayList<>(genreSet);

			Flux<Genre> genreFlux = genreService.saveAll(toSaveGenres);
			Set<Genre> set = new HashSet<>(
				Objects.requireNonNull(genreFlux.collectList().block()));
			Mono<Book> mono = book.map(b -> {
				b.setGenres(set);
				return b;
			});
			return repository.save(mono.block());
        }
        return null;
    }

}
