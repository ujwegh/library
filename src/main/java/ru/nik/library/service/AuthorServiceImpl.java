package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Author;
import ru.nik.library.repository.datajpa.AuthorRepository;

import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private AuthorRepository repository;


    public AuthorServiceImpl(AuthorRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<Author> addAuthor(String name) {
        return repository.save(new Author(name));
    }

    @Override
    public Mono<Boolean> deleteAuthorById(String id) {
        return repository.deleteById(id);
    }

    @Override
    public Mono<Boolean> deleteAuthorByName(String name) {
        return repository.deleteByName(name);
    }

    @Override
    public Mono<Author> updateAuthor(String id, String name) {
        return repository.findById(id).doOnSuccess(author -> {
            author.setName(name);
            repository.save(author);
        });
    }

    @Override
    public Mono<Author> getAuthorByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public Mono<Author> getAuthorById(String id) {
        return repository.findById(id);
    }

    @Override
    public Flux<Author> getAllAuthors() {
        return repository.findAll();
    }

    @Override
    public Flux<Author> getAllByNames(String... names) {
        return repository.findAllByNameIn(names);
    }

    @Override
    public Flux<Author> saveAll(List<Author> authors) {
        return repository.saveAll(authors);
    }
}
