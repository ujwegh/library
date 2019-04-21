package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Comment;
import ru.nik.library.repository.CommentDao;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentDao dao;

    @Autowired
    public CommentServiceImpl(CommentDao dao) {
        this.dao = dao;
    }


    @Override
    public Integer addComment(int bookId, String message) {
        Comment comment = new Comment(message);
        return dao.insert(comment, bookId);
    }

    @Override
    public Integer deleteCommentById(int id, int bookId) {
        return dao.deleteById(id, bookId);
    }

    @Override
    public Integer updateBookComment(int id, int bookId, String message) {
        Comment comment = dao.getById(id, bookId);
        comment.setComment(message);
        return dao.insert(comment, bookId);
    }

    @Override
    public Comment getCommentById(int id, int bookId) {
        return dao.getById(id, bookId);
    }

    @Override
    public List<Comment> getAllComments(int bookId) {
        return dao.getAll(bookId);
    }
}
