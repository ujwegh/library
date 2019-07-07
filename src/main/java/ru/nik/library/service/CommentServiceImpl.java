package ru.nik.library.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Comment;
import ru.nik.library.repository.datajpa.BookRepository;
import ru.nik.library.repository.datajpa.CommentRepository;

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
	public Mono<Comment> addComment(String bookId, String message) {
		Comment comment = new Comment(message);
		return bookRepository.findById(bookId).doOnSuccess(comment::setBook)
			.then(repository.save(comment));
	}

	@Override
	public Mono<Void> deleteCommentById(String id, String bookId) {
		return repository.deleteByIdAndBook_Id(id, bookId).then();
	}

	@Override
	public Mono<Comment> updateBookComment(String id, String bookId, String message) {
		return repository.findByIdAndBook_Id(id, bookId).flatMap(comment -> {
			comment.setComment(message);
			return repository.save(comment);
		});
	}

	@Override
	public Mono<Comment> getCommentById(String id, String bookId) {
		return repository.findByIdAndBook_Id(id, bookId);
	}

	@Override
	public Flux<Comment> getAllComments(String bookId) {
		return repository.findAllByBook_Id(bookId);
	}
}
