package ru.nik.library.service;

import ru.nik.library.domain.Book;

import java.util.List;

public interface BookService {
    Boolean addBook(String name, String description);

    Boolean deleteBookById(String id);

    Boolean updateBook(String id,String name, String description);

    Book getBookById(String id);

    List<Book> getAllBooks();

    Boolean updateBookAuthors(String bookId, String ... authors);

    Boolean updateBookGenres(String bookId, String ... genres);
}
