package com.psawesome.basepackage.rabbitmq.lrpart_06.images.repo;

import com.psawesome.basepackage.rabbitmq.lrpart_06.images.dto.Comment;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.comments.repo
 * author: PS
 * DATE: 2020-01-08 수요일 21:50
 */
public interface CommentReaderRepository extends Repository<Comment, String> {

    Flux<Comment> findByImageId(String imageId);
}
