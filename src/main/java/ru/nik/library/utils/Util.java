package ru.nik.library.utils;

import reactor.core.publisher.Flux;
import ru.nik.library.domain.Author;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class Util {
    public static String getAuthorNames(Set<Author> authors) {
        StringBuilder builder = new StringBuilder();
        authors.forEach(a -> builder.append(a.getName()).append(", "));
        return builder.substring(0, builder.toString().length() - 2);
    }

    public static String getGenreNames(Set<Genre> genres) {
        StringBuilder builder = new StringBuilder();
        genres.forEach(a -> builder.append(a.getName()).append(", "));
        return builder.substring(0, builder.toString().length() - 2);
    }

    public static Flux<Book> sortBooks(Flux<Book> books) {
        return books.sort(Comparator.comparing(Book::getName));
    }
}
