package ru.nik.library.domain;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "genres")
@Data
public class Genre{

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    @DBRef
    private Set<Book> books = new HashSet<>();

    public Genre(String name) {
        this.name = name;
    }

    @PersistenceConstructor
    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
