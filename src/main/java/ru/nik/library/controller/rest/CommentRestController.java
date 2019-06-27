package ru.nik.library.controller.rest;

import java.util.List;
import java.util.logging.Logger;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.nik.library.domain.Comment;
import ru.nik.library.service.CommentService;

@RestController
public class CommentRestController {
    private static Logger log = Logger.getLogger(CommentRestController.class.getName());
    private final CommentService service;

    @Autowired
    public CommentRestController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/rest/comments/{bookId}")
    public List<Comment> getAllComments(@PathVariable Integer bookId) {
        log.info("Get all comments");
        return service.getAllComments(bookId);
    }

    @DeleteMapping("/rest/comments/{bookId}/comment/{id}")
    public void deleteComment(@PathVariable Integer id, @PathVariable Integer bookId) {
        log.info("Delete comment: " + id + ", book id: " + bookId);
        service.deleteCommentById(id, bookId);
    }

    @PutMapping("/rest/comments/{bookId}")
    public Comment updateComment(@RequestBody Comment comment, @PathVariable Integer bookId) {
        log.info("Update comment: " + comment.getId() + " name = " + comment);
        if (service.updateBookComment(comment.getId(), bookId, comment.getComment()))
            return comment;
        else return null;
    }

    @PostMapping("/rest/comments/{bookId}")
    public Comment addComment(@RequestBody Comment comment, @PathVariable Integer bookId) {
        log.info("Add comment: " + comment + ", book id: " + bookId);
        if (service.addComment(bookId, comment.getComment()))
            return comment;
        else return null;
    }
}
