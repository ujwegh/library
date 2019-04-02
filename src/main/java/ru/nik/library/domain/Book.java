package ru.nik.library.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class Book extends BaseEntity{

    private String name;

    private String description;

    private Author author;

    private Genre genre;

    public Book(Book book) {
        this(book.getId(), book.getName(), book.getDescription(), book.getAuthor(), book.getGenre());
    }

    public Book(Integer id, String name, String description, Author author, Genre genre) {
        super(id);
        this.name = name;
        this.description = description;
        this.author = author;
        this.genre = genre;
    }

    public Book(String name, String description, Author author, Genre genre) {
        this(null, name, description, author, genre);
    }
}
