package com.psawesome.basepackage.rabbitmq.lrpart_06.comments.controllers;

import com.psawesome.basepackage.rabbitmq.lrpart_06.comments.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import reactor.core.publisher.Mono;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.comments.controller
 * author: PS
 * DATE: 2020-01-08 수요일 21:48
 */
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/comments")
    public Mono<String> addComment(Mono<Comment> newComment) {
        return newComment.flatMap(comment ->
                /*
                    Mono.fromRunnable() 의 Runnable 은 java.lang.Runnable 과 다르다.
                    run() 메서드가 스케줄러 내부에서 호출될 때
                    리액터가 정확하게 제어하는 편리한 래퍼를 제공

                    // TODO onAssembly() 이 이해가 되지 않는다.
                */
                Mono.fromRunnable(() ->
                        rabbitTemplate.convertAndSend(
                        "learning-spring-boot",
                        "comments.new", //routing key
                        comment
                )))
                .log("commentService-publish")
                .then(Mono.just("redirect:/"));
    }
}
