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
import ru.nik.library.dto.BookDto;
import ru.nik.library.service.BookService;

@RestController
public class BookFluxController {

	private static Logger log = Logger.getLogger(BookFluxController.class.getName());
	private final BookService service;

	@Autowired
	public BookFluxController(BookService service) {
		this.service = service;
	}

	@GetMapping("/rest/books")
	public Flux<BookDto> getAllBooks() {
		log.info("Get all books");
		return service.getAllBooks().map(BookDto::toBookDto);
	}

	@DeleteMapping("/rest/books/{id}")
	public Mono<Void> deleteBook(@PathVariable String id) {
		log.info("Delete book: " + id);
		return service.deleteBookById(id).then();
	}

	@PutMapping("/rest/books/{id}")
	public Mono<BookDto> updateBook(@RequestBody BookDto bookDto, @PathVariable String id) {
		log.info("Update book: id = " + id + ", name = " + bookDto.getName() + ", description = "
			+ bookDto.getDescription());
		return service.updateBook(bookDto.getId(), bookDto.getName(), bookDto.getDescription()).map(
			BookDto::toBookDto);
	}

	@PostMapping("/rest/books")
	public Mono<BookDto> addBook(@RequestBody BookDto bookDto) {
		log.info("Add book: name =" + bookDto.getName() + ", description = " + bookDto.getDescription());
		return service.addBook(bookDto.getName(), bookDto.getDescription()).map(BookDto::toBookDto);
	}


	@PostMapping("/rest/books/{id}/authors")
	public Mono<BookDto> updateBookAuthors(@RequestBody String string, @PathVariable String id) {
		String[] authors = string.split(", ");
		return service.updateBookAuthors(id, authors).map(BookDto::toBookDto);
	}

	@PostMapping("/rest/books/{id}/genres")
	public Mono<BookDto> updateBookGenres(@RequestBody String string, @PathVariable String id) {
		String[] genres = string.split(", ");
		return service.updateBookGenres(id, genres).map(BookDto::toBookDto);
	}

}
