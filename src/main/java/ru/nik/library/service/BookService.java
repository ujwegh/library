package ru.nik.library.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Book;

public interface BookService {
    Mono<Book> addBook(String name, String description);

    Mono<Void> deleteBookById(String id);

    Mono<Book> updateBook(String id,String name, String description);

    Mono<Book> getBookById(String id);

    Flux<Book> getAllBooks();

    Mono<Book> updateBookAuthors(String bookId, String ... authors);

    Mono<Book> updateBookGenres(String bookId, String ... genres);
}
