package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Genre;
import ru.nik.library.repository.datajpa.GenreRepository;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

	private final GenreRepository repository;

	@Autowired
	public GenreServiceImpl(GenreRepository repository) {
		this.repository = repository;
	}

	@Override
	public Mono<Genre> addGenre(String name) {
		return repository.save(new Genre(name));
	}

	@Override
	public Mono<Boolean> deleteGenreById(String id) {

		return repository.deleteById(id);

	}

	@Override
	public Mono<Boolean> deleteGenreByName(String name) {
		return repository.deleteByName(name);
	}

	@Override
	public Mono<Genre> updateGenre(String id, String name) {
		return repository.findById(id).doOnSuccess(genre -> {
			genre.setName(name);
			repository.save(genre);
		});
	}

	@Override
	public Mono<Genre> getGenreByName(String name) {
		return repository.findByName(name);
	}

	@Override
	public Mono<Genre> getGenreById(String id) {
		return repository.findById(id);
	}

	@Override
	public Flux<Genre> getAllGenres() {
		return repository.findAll();
	}

	@Override
	public Flux<Genre> getAllByNames(String... names) {
		return repository.findAllByNameIn(names);
	}

	@Override
	public Flux<Genre> saveAll(List<Genre> genres) {
		return repository.saveAll(genres);
	}
}
