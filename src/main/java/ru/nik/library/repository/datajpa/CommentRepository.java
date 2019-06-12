package ru.nik.library.repository.datajpa;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nik.library.domain.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, Integer> {

    List<Comment> findAllByBook_Id(String bookId);

    Comment findByIdAndBook_Id(String id, String bookId);

    int deleteByIdAndBook_Id(String id, String bookId);

}
