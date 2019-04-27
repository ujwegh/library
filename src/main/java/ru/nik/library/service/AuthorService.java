package ru.nik.library.service;

import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorService {
    Integer addAuthor(String name);

    Integer deleteAuthorById(int id);

    Integer deleteAuthorByName(String name);

    Integer updateAuthor(int id, String name);

    Author getAuthorByName(String name);

    Author getAuthorById(int id);

    List<Author> getAllAuthors();

    List<Author> getAllByNames(String ... names);

}
