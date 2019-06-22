package ru.nik.library.repository;

import ru.nik.library.domain.Comment;

import java.util.List;

public interface CommentDao {

    boolean insert(Comment comment, int bookId);

    Comment update(Comment comment, int bookId);

    Comment getById(int id, int bookId);

    List<Comment> getAll(int bookId);

    boolean deleteById(int id, int bookId);
}
