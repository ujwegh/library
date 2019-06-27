package ru.nik.library.controller.rest;

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
import ru.nik.library.dto.AuthorDto;
import ru.nik.library.service.AuthorService;

@RestController
public class AuthorFluxController {

	private static Logger log = Logger.getLogger(AuthorFluxController.class.getName());
	private final AuthorService service;

	@Autowired
	public AuthorFluxController(AuthorService service) {
		this.service = service;
	}

	@GetMapping("/rest/authors")
	public Flux<AuthorDto> getAuthors() {
		log.info("Get all authors");
		return service.getAllAuthors().map(AuthorDto::toAuthorDto);
	}

	@DeleteMapping("/rest/authors/{id}")
	public Mono<Void> deleteAuthor(@PathVariable String id){
		log.info("Delete author: " + id);
		return service.deleteAuthorById(id).then();
	}

	@PutMapping("/rest/authors/{id}")
	public Mono<AuthorDto> updateAuthor(@RequestBody AuthorDto author, @PathVariable String id) {
		log.info("Update author: id = " + id + " name = " + author.getName());
		return service.updateAuthor(id, author.getName()).map(AuthorDto::toAuthorDto);
	}

	@PostMapping("/rest/authors")
	public Mono<AuthorDto> addAuthor(@RequestBody AuthorDto author) {
		log.info("Add author: " + author.getName());
		return service.addAuthor(author.getName()).map(AuthorDto::toAuthorDto);
	}
}
