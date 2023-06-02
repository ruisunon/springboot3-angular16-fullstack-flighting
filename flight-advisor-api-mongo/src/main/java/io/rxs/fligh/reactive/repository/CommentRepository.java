package io.rxs.fligh.reactive.repository;

import io.rxs.fligh.reactive.model.Comment;
import io.rxs.fligh.reactive.model.PostId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CommentRepository extends ReactiveMongoRepository<Comment, String> {

    // @Tailable
    Flux<Comment> findByPost(PostId id);

    Mono<Long> countByPost(PostId id);

}