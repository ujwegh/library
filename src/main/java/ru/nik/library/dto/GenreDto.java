package ru.nik.library.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nik.library.domain.Genre;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {

    private String id;

    private String name;

    private List<String> bookNames;

    public static GenreDto toGenreDto(Genre genre) {
        List<String> bookNames = new ArrayList<>();
        genre.getBooks().forEach(b -> bookNames.add(b.getName()));
        return new GenreDto(genre.getId(), genre.getName(), bookNames);
    }
}
