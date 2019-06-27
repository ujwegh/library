package ru.nik.library.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nik.library.domain.Book;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String id;

    private String name;

    private String description;

    private Integer commentCount;

    private List<String> authorNames;

    private List<String> genreNames;

    public BookDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public static BookDto toBookDto(Book book) {
        List<String> authorNames = new ArrayList<>();
        List<String> genreNames = new ArrayList<>();
        book.getAuthors().forEach(a -> authorNames.add(a.getName()));
        book.getGenres().forEach(a -> genreNames.add(a.getName()));

        int commentSize = book.getComments() != null ? book.getComments().size() : 0;

        return new BookDto(book.getId(), book.getName(), book.getDescription(), commentSize, authorNames, genreNames);
    }
}
