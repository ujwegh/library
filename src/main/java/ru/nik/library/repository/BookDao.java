package ru.nik.library.repository;

import ru.nik.library.domain.Book;

import java.util.List;

public interface BookDao {
    int insert(Book book);

    Book getById(int id);

    List<Book> getAll();

    int deleteById(int id);

}
