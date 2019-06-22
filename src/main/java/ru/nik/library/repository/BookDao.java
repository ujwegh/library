package ru.nik.library.repository;

import ru.nik.library.domain.Book;

import java.util.List;

public interface BookDao {
    boolean insert(Book book);

    Book update(Book book);

    Book getById(int id);

    List<Book> getAll();

    boolean deleteById(int id);

}
