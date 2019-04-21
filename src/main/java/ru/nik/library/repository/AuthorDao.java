package ru.nik.library.repository;

import ru.nik.library.domain.Author;

import java.util.List;

public interface AuthorDao {
    int insert(Author author);

    Author getById(int id);

    Author getByName(String name);

    List<Author> getAll();

    int deleteById(int id);

    int deleteByName(String name);
}
