package com.psawesome.basepackage.rabbitmq.lrpart_06.comments.repo;

import com.psawesome.basepackage.rabbitmq.lrpart_06.comments.entity.Comment;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Mono;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.comments.entity
 * author: PS
 * DATE: 2020-01-08 수요일 21:48
 */
public interface CommentWriterRepository extends Repository<Comment, String> {

    Mono<Comment> save(Comment newComment);

    // 보조 save
    Mono<Comment> findById(String id);
}
