package ru.nik.library.service;

import ru.nik.library.domain.Comment;

import java.util.List;

public interface CommentService {
    Integer addComment(int bookId, String comment);

    Integer deleteCommentById(int id, int bookId);

    Integer updateBookComment(int id, int bookId, String comment);

    Comment getCommentById(int id, int bookId);

    List<Comment> getAllComments(int bookId);
}
