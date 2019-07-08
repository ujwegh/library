package ru.nik.library.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nik.library.domain.Book;
import ru.nik.library.service.BookService;

import static ru.nik.library.utils.Util.getAuthorNames;
import static ru.nik.library.utils.Util.getGenreNames;
import static ru.nik.library.utils.Util.sortBooks;


@Controller
@Slf4j
public class BookController {

	private final BookService service;

	@Autowired
	public BookController(BookService service) {
		this.service = service;
	}

	@GetMapping("/books")
	String index(Model model) {
		log.info("Get all books");
		model.addAttribute("books", sortBooks(service.getAllBooks()));
		return "welcome";
	}

	@GetMapping("/books/edit/{id}")
	public String edit(@PathVariable("id") int id, Model model) {
		log.info("Edit book: " + id);
		Book book = service.getBookById(id);
		String authorNames = null;
		String genreNames = null;
		if (book.getAuthors() != null && book.getAuthors().size() > 0) {
			authorNames = getAuthorNames(book.getAuthors());
		}
		if (book.getGenres() != null && book.getGenres().size() > 0) {
			genreNames = getGenreNames(book.getGenres());
		}
		model.addAttribute("authors", authorNames);
		model.addAttribute("genres", genreNames);
		model.addAttribute("book", book);
		return "/edit";
	}

	@GetMapping("/books/view/{id}")
	public String view(@PathVariable("id") int id, Model model) {
		log.info("View book: " + id);
		Book book = service.getBookById(id);
		String authorNames = null;
		String genreNames = null;
		if (book.getAuthors() != null && book.getAuthors().size() > 0) {
			authorNames = getAuthorNames(book.getAuthors());
		}
		if (book.getGenres() != null && book.getGenres().size() > 0) {
			genreNames = getGenreNames(book.getGenres());
		}
		model.addAttribute("authors", authorNames);
		model.addAttribute("genres", genreNames);
		model.addAttribute("book", book);
		return "/viewbook";
	}


	@PostMapping("/books/delete")
	public String delete(@RequestParam("id") int id, Model model) {
		log.info("Delete book: " + id);
		service.deleteBookById(id);
		model.addAttribute("books", sortBooks(service.getAllBooks()));
		return "redirect:/books";
	}

	@PostMapping("/books")
	public String addBook(@ModelAttribute("name") String name,
		@ModelAttribute("description") String description) {
		log.info("Add book: name =" + name + ", description = " + description);
		service.addBook(name, description);
		return "redirect:/books";
	}

	@PostMapping("/books/update")
	public String updateBook(@RequestParam("id") int id, @ModelAttribute("name") String name,
		@ModelAttribute("description") String description) {
		log.info("Update book: id = " + id + ", name = " + name + ", description = " + description);
		service.updateBook(id, name, description);
		return "redirect:/books";
	}

	@PostMapping("/books/update/authors")
	public String updateBookAuthors(@RequestParam("id") int id,
		@ModelAttribute("authors") String authors) {
		log.info("Update authors of book : id = " + id + ", authors = " + authors);

		String[] authorNames = authors.split(", ");
		service.updateBookAuthors(id, authorNames);
		return "redirect:/books";
	}

	@PostMapping("/books/update/genres")
	public String updateBookGenres(@RequestParam("id") int id,
		@ModelAttribute("genres") String genres) {
		log.info("Update genres of book : id = " + id + ", genres = " + genres);

		String[] genreNames = genres.split(", ");
		service.updateBookGenres(id, genreNames);
		return "redirect:/books";
	}


}
