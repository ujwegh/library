package ru.nik.library.service;

import org.springframework.stereotype.Service;
import ru.nik.library.domain.Comment;

@Service
public class CommentChanger {

	public Comment change(Comment comment) {
		String msg = comment.getComment();
		if (msg.contains("дурак")){
			msg = msg.replace("дурак", "ОЧЕНЬ ХОРОШИЙ ЧЕЛОВЕК");
			comment.setComment(msg);
		}
		return comment;
	}
}
