package ru.nik.library.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "authors")
@Data
public class Author {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @DBRef
    private Set<Book> books = new HashSet<>();

    public Author(String name) {
        this.name = name;
    }

    @PersistenceConstructor
    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
