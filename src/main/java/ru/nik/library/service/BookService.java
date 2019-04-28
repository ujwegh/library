package ru.nik.library.service;

import ru.nik.library.domain.Book;

import java.util.List;

public interface BookService {
    Boolean addBook(String name, String description);

    Boolean deleteBookById(int id);

    Boolean updateBook(int id,String name, String description);

    Book getBookById(int id);

    List<Book> getAllBooks();

    Boolean updateBookAuthors(int bookId, String ... authors);

    Boolean updateBookGenres(int bookId, String ... genres);
}
