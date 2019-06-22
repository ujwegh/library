package ru.nik.library.repository.datajpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.repository.CrudRepository;
import ru.nik.library.domain.Book;

import java.util.List;

public interface BookRepository extends CrudRepository<Book, Integer> {

    List<Book> findAll();

    Book findById(int id);

    void deleteById(int id) throws EmptyResultDataAccessException;
}
