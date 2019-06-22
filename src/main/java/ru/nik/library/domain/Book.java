package ru.nik.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
//@ToString
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Lazy
    @ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "map_books_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors = new HashSet<>();

    @Lazy
    @ManyToMany( cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "map_books_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();


    public Book(Integer id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Book(String name, String description) {
        this(null, name, description);
    }

    public boolean isNew() {
        return getId() == null;
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
