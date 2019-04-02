package ru.nik.library.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Genre extends BaseEntity{

    private String name;

    public Genre(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public Genre(String name) {
        this(null, name);
    }
}
