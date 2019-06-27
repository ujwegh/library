package ru.nik.library.repository.datajpa;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.nik.library.domain.Comment;

public interface CommentRepository extends ReactiveMongoRepository<Comment, Integer> {

	Flux<Comment> findAllByBook_Id(String bookId);

	Mono<Comment> findByIdAndBook_Id(String id, String bookId);

	Mono<Boolean> deleteByIdAndBook_Id(String id, String bookId);

}
