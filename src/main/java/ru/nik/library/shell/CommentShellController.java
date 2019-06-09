package ru.nik.library.shell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.nik.library.domain.Comment;
import ru.nik.library.service.CommentService;

import java.util.List;

@ShellComponent
public class CommentShellController {

    private final CommentService service;

    @Autowired
    public CommentShellController(CommentService service) {
        this.service = service;
    }

    @ShellMethod("allComments")
    public String allcomments(@ShellOption String bookId) {

        List<Comment> comments = service.getAllComments(bookId);
        if (comments != null) {
            return "Comments for book with id: " + bookId + " are " + comments.toString();
        }
        return "Not found comments for book with id: " + bookId;
    }

    @ShellMethod("getcomment")
    public String getcomment(@ShellOption String id, @ShellOption String bookId) {
        Comment comment = service.getCommentById(id, bookId);
        if (comment != null) {
            return comment.toString();
        }
        return "Not found comment with id: " + id + " and bookId: " + bookId;
    }

    @ShellMethod("addcomment")
    public String addcomment(@ShellOption String bookId, @ShellOption String comment) {
        boolean b = service.addComment(bookId, comment);
        if (!b) {
            return "Comment " + comment + " adding attempt has been failed.";
        }
        return "Comment " + comment + " successfully added.";
    }

    @ShellMethod("updatecomment")
    public String updatecomment(@ShellOption String id, @ShellOption String bookId,
                                @ShellOption String comment) {
        boolean b = service.updateBookComment(id, bookId, comment);
        if (!b) {
            return "Comment with id: " + id + " and book with id: " + bookId + " updating attempt has been failed.";
        }
        return "Comment with id: " + id + " and book with id: " + bookId + " successfully updated.";
    }

    @ShellMethod("deletecomment")
    public String deletecomment(@ShellOption String id, @ShellOption String bookId) {
        boolean b = service.deleteCommentById(id, bookId);
        if (!b) {
            return "Comment with id: " + id + " and book id: " + bookId + " deleting attempt has been failed.";
        }
        return "Comment with id: " + id + " and book id: " + bookId + " successfully deleted.";
    }
}
