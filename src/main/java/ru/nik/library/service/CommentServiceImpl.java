package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nik.library.domain.Book;
import ru.nik.library.domain.Comment;
import ru.nik.library.repository.datajpa.BookRepository;
import ru.nik.library.repository.datajpa.CommentRepository;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository repository;
    private final BookRepository bookRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository repository, BookRepository bookRepository) {
        this.repository = repository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Boolean addComment(int bookId, String message) {
        Comment comment = new Comment(message);
        Book book = bookRepository.findById(bookId);
        if (book != null){
            comment.setBook(book);
        } else {
            return false;
        }
        return repository.save(comment) != null;
    }

    @Override
    public Boolean deleteCommentById(int id, int bookId) {
        return repository.deleteByIdAndBook_Id(id, bookId) != 0;
    }

    @Override
    public Boolean updateBookComment(int id, int bookId, String message) {
        Comment comment = repository.findByIdAndBook_Id(id, bookId);
        comment.setComment(message);
        return repository.save(comment) != null;
    }

    @Override
    public Comment getCommentById(int id, int bookId) {
        return repository.findByIdAndBook_Id(id, bookId);
    }

    @Override
    public List<Comment> getAllComments(int bookId) {
        return repository.findAllByBook_Id(bookId);
    }
}
