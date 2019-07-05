package ru.nik.library.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.dto.GenreDto;
import ru.nik.library.service.GenreService;

@RestController
public class GenreFluxController {
	private static Logger log = Logger.getLogger(GenreFluxController.class.getName());
	private final GenreService service;

	@Autowired
	public GenreFluxController(GenreService service) {
		this.service = service;
	}

	@GetMapping("/rest/genres")
	public Flux<GenreDto> getAuthors() {
		log.info("Get all genres");
		return service.getAllGenres().map(GenreDto::toGenreDto);
	}

	@DeleteMapping("/rest/genres/{id}")
	public Mono<Void> deleteAuthor(@PathVariable String id){
		log.info("Delete genre: " + id);
		return service.deleteGenreById(id).then();
	}

	@PutMapping("/rest/genres/{id}")
	public Mono<GenreDto> updateAuthor(@RequestBody GenreDto author, @PathVariable String id) {
		log.info("Update genre: id = " + id + " name = " + author.getName());
		return service.updateGenre(id, author.getName()).map(GenreDto::toGenreDto);
	}

	@PostMapping("/rest/genres")
	public Mono<GenreDto> addAuthor(@RequestBody GenreDto author) {
		log.info("Add genre: " + author.getName());
		return service.addGenre(author.getName()).map(GenreDto::toGenreDto);
	}

}
