package ru.nik.library.service;

import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorService {
    Boolean addAuthor(String name);

    Boolean deleteAuthorById(String id);

    Boolean deleteAuthorByName(String name);

    Boolean updateAuthor(String id, String name);

    Author getAuthorByName(String name);

    Author getAuthorById(String id);

    List<Author> getAllAuthors();

    List<Author> getAllByNames(String ... names);

    List<Author> saveAll(List<Author> authors);

}
