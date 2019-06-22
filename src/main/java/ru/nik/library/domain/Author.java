package ru.nik.library.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Author extends BaseEntity{

    private String name;

    public Author(Author author) {
        this(null, author.getName());
    }

    public Author(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Author(String name) {
        this(null, name);
    }
}
