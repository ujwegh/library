package ru.nik.library.controller.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nik.library.dto.GenreDto;
import ru.nik.library.service.GenreService;

@RestController
@Transactional
public class GenreRestController {
    private static Logger log = Logger.getLogger(GenreRestController.class.getName());
    private final GenreService service;

    @Autowired
    public GenreRestController(GenreService service) {
        this.service = service;
    }

    @GetMapping("/rest/genres/all")
    public List<GenreDto> getGenres() {
        log.info("Get all genres");
        List<GenreDto> result = new ArrayList<>();
        service.getAllGenres().forEach(a-> result.add(GenreDto.toGenreDto(a)));
        return result;
    }

    @DeleteMapping("/rest/genres/genre/{id}")
    public void deleteGenre(@PathVariable Integer id){
        log.info("Delete genre: " + id);
        service.deleteGenreById(id);
    }

    @PutMapping("/rest/genres/genre/{id}")
    public GenreDto updateGenre(@RequestBody GenreDto genre, @PathVariable Integer id) {
        log.info("Update genre: id = " + id + " name = " + genre.getName());
        if (service.updateGenre(id, genre.getName())) {
            return genre;
        } else {
            return null;
        }
    }

    @PostMapping("/rest/genres")
    public GenreDto addGenre(@RequestBody GenreDto genre) {
        log.info("Add genre: " + genre.getName());
        if (service.addGenre(genre.getName())){
            return genre;
        } else return null;
    }


}
