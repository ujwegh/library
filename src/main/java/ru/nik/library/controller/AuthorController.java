package ru.nik.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.nik.library.domain.Author;
import ru.nik.library.service.AuthorService;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class AuthorController {

    private static Logger log = Logger.getLogger(AuthorController.class.getName());
    private final AuthorService service;

    @Autowired
    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping("/authors")
    public String getAuthors(Model model) {
        log.info("Get all authors");
        List<Author> authors = service.getAllAuthors();
        model.addAttribute("authors", authors);
        return "list";
    }

    @GetMapping("/authors/edit/{id}")
    public String edit(@PathVariable("id") int id, Model model) {
        log.info("Edit author: " + id);
        Author author = service.getAuthorById(id);
        model.addAttribute("author", author);
        return "/edit";
    }

    @PostMapping("/authors/delete")
    public String delete(@RequestParam("id") int id) {
        log.info("Delete author: " + id);
        service.deleteAuthorById(id);
        return "redirect:/authors";
    }

    @PostMapping("/authors")
    public String addAuthor(@RequestParam("name") String name) {
        log.info("Add author: " + name);
        service.addAuthor(name);
        return "redirect:/authors";
    }

    @PostMapping("/authors/update")
    public String updateAuthor(@RequestParam("id") int id, @ModelAttribute("name") String name) {
        log.info("Update author: id = " + id + " name = " + name);
        service.updateAuthor(id, name);
        return "redirect:/authors";
    }

}
