package ru.nik.library.service;

import ru.nik.library.domain.Comment;

import java.util.List;

public interface CommentService {
    Boolean addComment(int bookId, String comment);

    Boolean deleteCommentById(int id, int bookId);

    Boolean updateBookComment(int id, int bookId, String comment);

    Comment getCommentById(int id, int bookId);

    List<Comment> getAllComments(int bookId);
}
