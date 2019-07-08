package ru.nik.library.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nik.library.domain.Comment;
import ru.nik.library.service.CommentService;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@Slf4j
public class CommentController {

	private final CommentService service;

	@Autowired
	public CommentController(CommentService service) {
		this.service = service;
	}

	@GetMapping("/comments/{bookId}")
	public String getComments(@PathVariable("bookId") int bookId, Model model) {
		log.info("Get all comments");
		List<Comment> comments = service.getAllComments(bookId);
		model.addAttribute("bookId", bookId);
		model.addAttribute("comments", comments);
		return "comments";
	}

	@GetMapping("/comments/{bookId}/edit/{id}")
	public String edit(@PathVariable("id") int id, @PathVariable("bookId") int bookId,
		Model model) {
		log.info("Edit comment: " + id + ", book id: " + bookId);
		Comment comment = service.getCommentById(id, bookId);
		model.addAttribute("bookId", bookId);
		model.addAttribute("comment", comment);
		return "/edit";
	}

	@PostMapping("/comments/{bookId}/delete")
	@Transactional
	public String delete(@RequestParam("id") int id, @PathVariable("bookId") int bookId) {
		log.info("Delete comment: " + id + ", book id: " + bookId);
		service.deleteCommentById(id, bookId);
		return "redirect:/comments/" + bookId;
	}

	@PostMapping("/comments/{bookId}")
	public String addComment(@PathVariable("bookId") int bookId,
		@RequestParam("comment") String comment) {
		log.info("Add comment: \"" + comment + "\", book id: " + bookId);
		service.addComment(bookId, comment);
		return "redirect:/comments/" + bookId;
	}

	@PostMapping("/comments/{bookId}/update")
	public String updateComment(@RequestParam("id") int id, @PathVariable("bookId") int bookId,
		@ModelAttribute("comment") String comment) {
		log.info("Update comment: " + id + " name = " + comment);
		service.updateBookComment(id, bookId, comment);
		return "redirect:/comments/" + bookId;
	}
}
