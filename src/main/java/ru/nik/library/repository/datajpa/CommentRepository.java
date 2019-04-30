package ru.nik.library.repository.datajpa;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.nik.library.domain.Comment;

import java.util.List;

/**
 * @author nik_aleks
 * @version 1.0
 */

public interface CommentRepository extends CrudRepository<Comment, Integer> {

    List<Comment> findAllByBook_Id(int bookId);

    Comment findByIdAndBook_Id(int id, int bookId);

    int deleteByIdAndBook_Id(int id, int bookId);

}
