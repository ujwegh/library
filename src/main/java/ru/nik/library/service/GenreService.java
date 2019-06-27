package ru.nik.library.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Genre;

import java.util.List;

public interface GenreService {
    Mono<Genre> addGenre(String name);

    Mono<Boolean> deleteGenreById(String id);

    Mono<Boolean> deleteGenreByName(String name);

    Mono<Genre> updateGenre(String id, String name);

    Mono<Genre> getGenreByName(String name);

    Mono<Genre> getGenreById(String id);

    Flux<Genre> getAllGenres();

    Flux<Genre> getAllByNames(String ... names);

    Flux<Genre> saveAll(List<Genre> genres);
}
