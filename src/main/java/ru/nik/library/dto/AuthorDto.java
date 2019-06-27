package ru.nik.library.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nik.library.domain.Author;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

    private String id;

    private String name;

    private List<String> bookNames;

    public static AuthorDto toAuthorDto(Author author) {
        List<String> bookNames = new ArrayList<>();
        author.getBooks().forEach(b -> bookNames.add(b.getName()));
        return new AuthorDto(author.getId(), author.getName(), bookNames);
    }
}
