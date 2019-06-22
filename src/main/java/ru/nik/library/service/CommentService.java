package ru.nik.library.service;

import ru.nik.library.domain.Comment;

import java.util.List;

public interface CommentService {
    Boolean addComment(String bookId, String comment);

    Boolean deleteCommentById(String id, String bookId);

    Boolean updateBookComment(String id, String bookId, String comment);

    Comment getCommentById(String id, String bookId);

    List<Comment> getAllComments(String bookId);
}
