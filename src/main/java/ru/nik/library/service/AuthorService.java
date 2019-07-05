package ru.nik.library.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorService {
    Mono<Author> addAuthor(String name);

    Mono<Void> deleteAuthorById(String id);

    Mono<Author> updateAuthor(String id, String name);

    Mono<Author> getAuthorByName(String name);

    Mono<Author> getAuthorById(String id);

    Flux<Author> getAllAuthors();

    Flux<Author> getAllByNames(String ... names);

    Flux<Author> saveAll(List<Author> authors);

}
