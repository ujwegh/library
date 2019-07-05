package ru.nik.library.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.nik.library.domain.Comment;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

	private String id;

	private String bookId;

	private String comment;

	public static CommentDto toCommentDto(Comment comment) {
		return new CommentDto(comment.getId(), (comment.getBook() != null ? comment.getBook().getId() : null),
			comment.getComment());
	}
}
