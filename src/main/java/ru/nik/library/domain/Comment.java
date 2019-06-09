package ru.nik.library.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "comments")
@Data
public class Comment{

    @Id
    private String id;

    private String comment;

    @DBRef
    private Book book;


    public Comment(String comment) {
        this.comment = comment;
    }

    @PersistenceConstructor
    public Comment(String id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
