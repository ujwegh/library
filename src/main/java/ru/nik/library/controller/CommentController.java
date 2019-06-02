package ru.nik.library.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nik.library.domain.Comment;
import ru.nik.library.service.CommentService;

import javax.transaction.Transactional;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class CommentController {

    private static Logger log = Logger.getLogger(CommentController.class.getName());
    private final CommentService service;


    @Autowired
    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/comments")
    public String getComments(@RequestParam("bookId") int bookId, Model model) {
        log.info("Get all comments");
        List<Comment> comments = service.getAllComments(bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("comments", comments);
        return "comments";
    }

    @GetMapping("/comments/edit")
    public String edit(@RequestParam("id") int id, @RequestParam("bookId") int bookId, Model model) {
        log.info("Edit comment: " + id + ", book id: " + bookId);
        Comment comment = service.getCommentById(id, bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("comment", comment);
        return "/edit";
    }

    @PostMapping("/comments/delete")
    @Transactional
    public String delete(@RequestParam("id") int id, @RequestParam("bookId") int bookId, Model model) {
        log.info("Delete comment: " + id + ", book id: " + bookId);
        service.deleteCommentById(id, bookId);
        List<Comment> comments = service.getAllComments(bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("comments", comments);
        return "redirect:/comments";
    }

    @PostMapping("/comments")
    public String addComment(@RequestParam("bookId") int bookId, @RequestParam("comment") String comment, Model model) {
        log.info("Add comment: \"" + comment + "\", book id: " + bookId);
        service.addComment(bookId, comment);
        List<Comment> comments = service.getAllComments(bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("comments", comments);
        return "redirect:/comments";
    }

    @PostMapping("/comments/update")
    public String updateAuthor(@RequestParam("id") int id,  @RequestParam("bookId") int bookId, @ModelAttribute("comment") String comment, Model model) {
        log.info("Update comment: " + id + " name = " + comment);
        service.updateBookComment(id, bookId, comment);
        List<Comment> comments = service.getAllComments(bookId);
        model.addAttribute("bookId", bookId);
        model.addAttribute("comments", comments);
        return "redirect:/comments";
    }
}
