package ru.nik.library.repository;

import org.springframework.dao.DataAccessException;
import ru.nik.library.domain.Book;

import java.util.List;

public interface BookDao {
    int insert(Book book);

    Book getById(int n) throws DataAccessException;

    List<Book> getAll();

    int deleteById(int id);
}
