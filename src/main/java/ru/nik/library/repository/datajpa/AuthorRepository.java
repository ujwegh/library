package ru.nik.library.repository.datajpa;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Author;

public interface AuthorRepository extends ReactiveMongoRepository<Author, Integer> {

    Flux<Author> findAll();

    Flux<Author> findAllByNameIn(String... names);

    Mono<Author> findByName(String name);

    Mono<Author> findById(String id);

    Mono<Boolean> deleteById(String id);

    Mono<Boolean> deleteByName(String name);
}
