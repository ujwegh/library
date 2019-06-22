package ru.nik.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@Getter
@Setter
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    private String name;

    private String description;

    @DBRef
    private List<Comment> comments;

    @DBRef
    private Set<Author> authors = new HashSet<>();

    @DBRef
    private Set<Genre> genres = new HashSet<>();

    public Book(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @PersistenceConstructor
    public Book(String id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public String getAuthorsNames() {
        StringBuilder builder = new StringBuilder();
        if (!authors.isEmpty() & authors.size() > 0) {
            authors.forEach(a -> builder.append(a.getName()).append(", "));
            return builder.toString().substring(0, builder.toString().length() - 2);
        }
        return "";
    }

    public String getGenresNames() {
        StringBuilder builder = new StringBuilder();
        if (!genres.isEmpty() & genres.size() > 0) {
            genres.forEach(a -> builder.append(a.getName()).append(", "));
            return builder.toString().substring(0, builder.toString().length() - 2);
        }
        return "";
    }

    @Override
    public String toString() {
        return "Book{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", comments=" + (comments != null ? comments.size() : null) +
            ", authors=" + (authors != null ? authors.size() : null) +
            ", genres=" + (genres != null ? genres.size() : null) +
            '}';
    }
}
