package com.psawesome.basepackage.rabbitmq.lrpart_06;

import com.psawesome.basepackage.rabbitmq.lrpart_06.comments.controllers.CommentController;
import com.psawesome.basepackage.rabbitmq.lrpart_06.comments.entity.Comment;
import com.psawesome.basepackage.rabbitmq.lrpart_06.images.repo.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06
 * author: PS
 * DATE: 2020-01-10 금요일 21:01
 */
@Profile("simulator")
@Component
@RequiredArgsConstructor
public class CommentSimulator {

    private final CommentController controller;
    private final ImageRepository repository;

    private final AtomicInteger counter = new AtomicInteger(1);

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        Flux.interval(Duration.ofMillis(1000))
                .flatMap(tick -> repository.findAll())
                .map(image -> {
                    Comment comment = new Comment();
                    comment.setImageId(image.getId());
                    comment.setComment("Comment #" + counter.getAndIncrement());
                    return Mono.just(comment);
                })
                .flatMap(newComment -> Mono.defer(() -> controller.addComment(newComment)))
                .subscribe();

    }
}
