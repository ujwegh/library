package ru.nik.library.controller.rest;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nik.library.dto.BookDto;
import ru.nik.library.service.BookService;

@RestController
@Slf4j
public class BookRestController {

	private final BookService service;

	@Autowired
	public BookRestController(BookService service) {
		this.service = service;
	}


	@GetMapping("/rest/books")
	public List<BookDto> getAllBooks() {
		log.info("Get all books");
		List<BookDto> result = new ArrayList<>();
		service.getAllBooks().forEach(b -> result.add(BookDto.toBookDto(b)));
		return result;
	}

	@DeleteMapping("/rest/books/{id}")
	public void deleteBook(@PathVariable Integer id) {
		log.info("Delete book: " + id);
		service.deleteBookById(id);
	}

	@PutMapping("/rest/books/{id}")
	public BookDto updateBook(@RequestBody BookDto bookDto, @PathVariable Integer id) {
		log.info("Update book: id = " + id + ", name = " + bookDto.getName() + ", description = "
			+ bookDto.getDescription());
		if (service.updateBook(bookDto.getId(), bookDto.getName(), bookDto.getDescription())) {
			return bookDto;
		} else {
			return null;
		}
	}

	@PostMapping("/rest/books")
	public BookDto addBook(@RequestBody BookDto bookDto) {
		log.info(
			"Add book: name =" + bookDto.getName() + ", description = " + bookDto.getDescription());
		if (service.addBook(bookDto.getName(), bookDto.getDescription())) {
			return bookDto;
		} else {
			return null;
		}
	}


	@PostMapping("/rest/books/{id}/authors")
	public void updateBookAuthors(@RequestBody String string, @PathVariable Integer id) {
		String[] authors = string.split(", ");
		service.updateBookAuthors(id, authors);
	}

	@PostMapping("/rest/books/{id}/genres")
	public void updateBookGenres(@RequestBody String string, @PathVariable Integer id) {
		String[] genres = string.split(", ");
		service.updateBookGenres(id, genres);
	}


}