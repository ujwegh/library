package ru.nik.library.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Comment;

public interface CommentService {
    Mono<Comment> addComment(String bookId, String comment);

    Mono<Boolean> deleteCommentById(String id, String bookId);

    Mono<Comment> updateBookComment(String id, String bookId, String comment);

    Mono<Comment> getCommentById(String id, String bookId);

    Flux<Comment> getAllComments(String bookId);
}
