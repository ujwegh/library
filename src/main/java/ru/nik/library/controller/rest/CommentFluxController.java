package ru.nik.library.controller.rest;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Comment;
import ru.nik.library.dto.CommentDto;
import ru.nik.library.service.CommentService;

@RestController
public class CommentFluxController {

	private static Logger log = Logger.getLogger(CommentFluxController.class.getName());
	private final CommentService service;

	@Autowired
	public CommentFluxController(CommentService service) {
		this.service = service;
	}

	@GetMapping("/rest/comments/{bookId}")
	public Flux<CommentDto> getAllComments(@PathVariable String bookId) {
		log.info("Get all comments");
		return service.getAllComments(bookId).map(CommentDto::toCommentDto);
	}

	@DeleteMapping("/rest/comments/{bookId}/comment/{id}")
	public Mono<Void> deleteComment(@PathVariable String id, @PathVariable String bookId) {
		log.info("Delete comment: " + id + ", book id: " + bookId);
		return service.deleteCommentById(id, bookId).then();
	}

	@PutMapping("/rest/comments/{bookId}")
	public Mono<CommentDto> updateComment(@RequestBody CommentDto comment, @PathVariable String bookId) {
		log.info("Update comment: " + comment.getId() + " name = " + comment);
		return service.updateBookComment(comment.getId(), bookId, comment.getComment()).map(
			CommentDto::toCommentDto);
	}

	@PostMapping("/rest/comments/{bookId}")
	public Mono<CommentDto> addComment(@RequestBody CommentDto comment, @PathVariable String bookId) {
		log.info("Add comment: " + comment + ", book id: " + bookId);
		return service.addComment(bookId, comment.getComment()).map(CommentDto::toCommentDto);
	}

}
