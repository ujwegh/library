package ru.nik.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
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
    public String getGenres(Model model) {
        log.info("Get all genres");
        Flux<Genre> genres = service.getAllGenres();
        model.addAttribute("genres", genres);
        return "list";
    }

    @GetMapping("/genres/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        log.info("Edit genre: " + id);
        Mono<Genre> genre = service.getGenreById(id);
        model.addAttribute("genre", genre);
        return "/edit";
    }

    @PostMapping("/genres/delete")
    public String delete(@RequestParam("id") String id) {
        log.info("Delete genre: " + id);
        service.deleteGenreById(id);
        return "redirect:/genres";
    }

    @PostMapping("/genres")
    public String addGenre(@RequestParam("name") String name) {
        log.info("Add genre: " + name);
        service.addGenre(name);
        return "redirect:/genres";
    }

    @PostMapping("/genres/update")
    public String updateGenre(@RequestParam("id") String id, @ModelAttribute("name") String name) {
        log.info("Update genre: id = " + id + " name = " + name);
        service.updateGenre(id, name);
        return "redirect:/genres";
    }




}
