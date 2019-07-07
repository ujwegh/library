package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
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
		return repository.findById(id).flatMap(book -> {
			book.setName(name);
			book.setDescription(description);
			 return repository.save(book);
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
        Set<Author> toAdd = new HashSet<>();
        Set<Author> toDelete = new HashSet<>();

		return repository.findById(bookId).flatMap(book1 -> {
			authorService.getAllByNames(authors).collectList().map(authors1 -> {

				authorSet.forEach(author -> authors1.forEach(existedAuthor -> {
						if (author.getName().equals(existedAuthor.getName())) {
							toDelete.add(author);
							toAdd.add(existedAuthor);
						}
					})
				);
				return authors1;
			});

			authorSet.removeAll(toDelete);
			authorSet.addAll(toAdd);
			List<Author> toSaveAuthors = new ArrayList<>(authorSet);

			return authorService.saveAll(toSaveAuthors).collectList().flatMap(authors1 -> {
				Set<Author> set = new HashSet<>(authors1);
				book1.setAuthors(set);
				return Mono.just(book1);
			}).flatMap(repository::save);
		});
    }

    @Override
    public Mono<Book> updateBookGenres(String bookId, String... genres) {
        Set<Genre> genreSet = Arrays.stream(genres).map(Genre::new).collect(Collectors.toSet());
        Set<Genre> toAdd = new HashSet<>();
        Set<Genre> toDelete = new HashSet<>();

		return repository.findById(bookId).flatMap(book1 -> {
			genreService.getAllByNames(genres).collectList().map(genres1 -> {

				genreSet.forEach(genre -> genres1.forEach(existedGenre -> {
						if (genre.getName().equals(existedGenre.getName())) {
							toDelete.add(genre);
							toAdd.add(existedGenre);
						}
					})
				);
				return genres1;
			});

			genreSet.removeAll(toDelete);
			genreSet.addAll(toAdd);
			List<Genre> toSaveGenres = new ArrayList<>(genreSet);

			return genreService.saveAll(toSaveGenres).collectList().flatMap(genres1 -> {
				Set<Genre> set = new HashSet<>(genres1);
				book1.setGenres(set);
				return Mono.just(book1);
			}).flatMap(repository::save);
		});
    }

}
