package ru.nik.library.repository.datajpa;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Book;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Flux<Book> findAll();

    Mono<Book> findById(String id);

    Mono<Void> deleteById(String id);
}
