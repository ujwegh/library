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
import ru.nik.library.dto.AuthorDto;
import ru.nik.library.service.AuthorService;

@RestController
@Transactional
public class AuthorRestController {
    private static Logger log = Logger.getLogger(AuthorRestController.class.getName());
    private final AuthorService service;

    @Autowired
    public AuthorRestController(AuthorService service) {
        this.service = service;
    }

    @GetMapping("/rest/authors/all")
    public List<AuthorDto> getAuthors() {
        log.info("Get all authors");
        List<AuthorDto> result = new ArrayList<>();
        service.getAllAuthors().forEach(a-> result.add(AuthorDto.toAuthorDto(a)));
        return result;
    }

    @DeleteMapping("/rest/authors/author/{id}")
    public void deleteAuthor(@PathVariable Integer id){
        log.info("Delete author: " + id);
        service.deleteAuthorById(id);
    }

    @PutMapping("/rest/authors/author/{id}")
    public AuthorDto updateAuthor(@RequestBody AuthorDto author, @PathVariable Integer id) {
        log.info("Update author: id = " + id + " name = " + author.getName());
        if (service.updateAuthor(id, author.getName())) {
            return author;
        } else {
            return null;
        }
    }

    @PostMapping("/rest/authors")
    public AuthorDto addAuthor(@RequestBody AuthorDto author) {
        log.info("Add author: " + author.getName());
        if (service.addAuthor(author.getName())){
            return author;
        } else return null;
    }
}
