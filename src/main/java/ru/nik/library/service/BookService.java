package ru.nik.library.service;

import ru.nik.library.domain.Book;

import java.util.List;

public interface BookService {
    Integer addBook(String name, String description, String author, String genre);

    Integer deleteBookById(int id);

    Integer updateBook(int id,String name, String description, String authorName, String genreName);

    Book getBookById(int id);

    List<Book> getAllBooks();
}
