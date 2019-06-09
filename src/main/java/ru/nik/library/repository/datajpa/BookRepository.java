package ru.nik.library.repository.datajpa;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import ru.nik.library.domain.Book;

import java.util.List;

public interface BookRepository extends MongoRepository<Book, Integer> {

    List<Book> findAll();

    Book findById(String id);

    void deleteById(String id) throws EmptyResultDataAccessException;
}
