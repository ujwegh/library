package ru.nik.library.repository.datajpa;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

	Flux<Comment> findAllByBook_Id(String bookId);

	Mono<Comment> findByIdAndBook_Id(String id, String bookId);

	Mono<Long> deleteByIdAndBook_Id(String id, String bookId);

}
