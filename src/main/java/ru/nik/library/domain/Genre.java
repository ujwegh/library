package ru.nik.library.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Entity
@Table(name = "genres")
@Data
public class Genre{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = true)
    private String name;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToMany(mappedBy = "genres", fetch = FetchType.LAZY)
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
