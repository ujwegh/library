package ru.nik.library.utils;

import ru.nik.library.domain.Author;

import java.util.Set;

public class Util {
    public static String getAuthorNames(Set<Author> authors) {
        StringBuilder builder = new StringBuilder();
        authors.forEach(a -> builder.append(a.getName()).append(", "));
        return builder.substring(0, builder.toString().length() - 3);
    }
}
