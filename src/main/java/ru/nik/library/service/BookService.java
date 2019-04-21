package ru.nik.library.service;

import ru.nik.library.domain.Book;

import java.util.List;

public interface BookService {
    Integer addBook(String name, String description);

    Integer deleteBookById(int id);

    Integer updateBook(int id,String name, String description);

    Book getBookById(int id);

    List<Book> getAllBooks();

    Integer updateBookAuthors(int bookId, String ... authors);

    Integer updateBookGenres(int bookId, String ... genres);
}
