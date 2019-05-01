package ru.nik.library.repository;

import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    boolean insert(Author author);

    Author update(Author author);

    Author getById(int id);

    Author getByName(String name);

    List<Author> getAll();

    boolean deleteById(int id);

    boolean deleteByName(String name);

    List<Author> getAllByNames(String ... names);
}
