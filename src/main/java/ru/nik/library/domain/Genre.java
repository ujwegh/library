package ru.nik.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "genres")
public class Genre{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Book> books = new HashSet<>();

    public Genre(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(String name) {
        this(null, name);
    }

    public boolean isNew() {
        return getId() == null;
    }

}
