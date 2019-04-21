package ru.nik.library.repository;

import ru.nik.library.domain.Comment;

import java.util.List;

public interface CommentDao {

    int insert(Comment comment, int bookId);

    Comment getById(int id, int bookId);

    List<Comment> getAll(int bookId);

    int deleteById(int id, int bookId);
}
