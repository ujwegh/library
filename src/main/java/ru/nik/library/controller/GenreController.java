package ru.nik.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nik.library.domain.Genre;
import ru.nik.library.service.GenreService;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class GenreController {

    private static Logger log = Logger.getLogger(GenreController.class.getName());
    private final GenreService service;

    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping("/genres")
    public String getAuthors(Model model) {
        log.info("Get all genres");
        List<Genre> genres = service.getAllGenres();
        model.addAttribute("genres", genres);
        return "list";
    }

    @GetMapping("/genres/edit")
    public String edit(@RequestParam("id") int id, Model model) {
        log.info("Edit genre: " + id);
        Genre genre = service.getGenreById(id);
        model.addAttribute("genre", genre);
        return "/edit";
    }

    @PostMapping("/genres/delete")
    public String delete(@RequestParam("id") int id, Model model) {
        log.info("Delete genre: " + id);
        service.deleteGenreById(id);
        List<Genre> genres = service.getAllGenres();
        model.addAttribute("genres", genres);
        return "list";
    }

    @PostMapping("/genres")
    public String addGenre(@RequestParam("name") String name, Model model) {
        log.info("Add genre: " + name);
        service.addGenre(name);
        List<Genre> genres = service.getAllGenres();
        model.addAttribute("genres", genres);
        return "list";
    }

    @PostMapping("/genres/update")
    public String updateGenre(@RequestParam("id") int id, @ModelAttribute("name") String name, Model model) {
        log.info("Update genre: id = " + id + " name = " + name);
        service.updateGenre(id, name);
        List<Genre> genres = service.getAllGenres();
        model.addAttribute("genres", genres);
        return "list";
    }




}