package ru.nik.library.service;

import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorService {
    Boolean addAuthor(String name);

    Boolean deleteAuthorById(int id);

    Boolean deleteAuthorByName(String name);

    Boolean updateAuthor(int id, String name);

    Author getAuthorByName(String name);

    Author getAuthorById(int id);

    List<Author> getAllAuthors();

    List<Author> getAllByNames(String ... names);

}
