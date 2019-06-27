package ru.nik.library.repository.datajpa;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, Integer> {

	Flux<Genre> findAll();

	Flux<Genre> findAllByNameIn(String... names);

	Mono<Genre> findByName(String name);

	Mono<Genre> findById(String id);

	Mono<Boolean> deleteById(String id);

	Mono<Boolean> deleteByName(String name);
}
