package ru.nik.library.repository.datajpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, Integer> {

    Flux<Book> findAll();

    Mono<Book> findById(String id);

    Mono<Boolean> deleteById(String id) throws EmptyResultDataAccessException;
}
