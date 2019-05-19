package ru.nik.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.service.BookService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static ru.nik.library.utils.Util.getAuthorNames;


@Controller
public class BookController {

    private static Logger log = Logger.getLogger(BookController.class.getName());
    private final BookService service;

    @Autowired
    public BookController(BookService service) {
        this.service = service;
    }

    @GetMapping("/")
    String index(Model model) {
        log.info("Get all books");
        List<Book> books = service.getAllBooks();
        model.addAttribute("books", books);
        return "welcome";
    }

    @GetMapping("/books/edit")
    public String edit(@RequestParam("id") int id, Model model) {
        log.info("Edit book: " + id);
        Book book = service.getBookById(id);
        String authorNames = null;
        if (book.getAuthors() != null && book.getAuthors().size() > 0) {
            authorNames = getAuthorNames(book.getAuthors());
        }
        model.addAttribute("authors", authorNames);
        model.addAttribute("book", book);
        return "/edit";
    }


    @PostMapping("/books/delete")
    public String delete(@RequestParam("id") int id, Model model) {
        log.info("Delete book: " + id);
        service.deleteBookById(id);
        List<Book> books = service.getAllBooks();
        model.addAttribute("books", books);
        return "welcome";
    }

    @PostMapping("/books")
    public String addBook(@ModelAttribute("name") String name, @ModelAttribute("description") String description, Model model) {
        log.info("Add book: name =" + name + ", description = " + description);
        service.addBook(name, description);
        List<Book> authors = service.getAllBooks();
        model.addAttribute("books", authors);
        return "welcome";
    }

    @PostMapping("/books/update")
    public String updateBook(@RequestParam("id") int id, @ModelAttribute("name") String name,
                             @ModelAttribute("description") String description, Model model) {
        log.info("Update book: id = " + id + ", name = " + name + ", description = " + description);
        service.updateBook(id, name, description);
        List<Book> books = service.getAllBooks();
        model.addAttribute("books", books);
        return "redirect:/";
    }

    @PostMapping("/books/update/authors")
    public String updateBookAuthors(@RequestParam("id") int id,
                                    @ModelAttribute("authors") String authors, Model model) {
        log.info("Update authors of book : id = " + id + ", authors = " + authors);

        System.out.println(authors);
        String[] authorNames = authors.split(", ");
        System.out.println(Arrays.toString(authorNames));
        service.updateBookAuthors(id, authorNames);

        List<Book> books = service.getAllBooks();
        model.addAttribute("books", books);
        return "redirect:/";
    }


}