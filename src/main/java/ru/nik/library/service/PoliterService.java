package ru.nik.library.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.nik.library.domain.Comment;

@MessagingGateway
public interface PoliterService {

	@Gateway(requestChannel = "commentsChannel", replyChannel = "politerChannel")
	Comment process(Comment comment);
}
