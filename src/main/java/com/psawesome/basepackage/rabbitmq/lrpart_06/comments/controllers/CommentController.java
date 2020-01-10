package com.psawesome.basepackage.rabbitmq.lrpart_06.comments.controllers;

import com.psawesome.basepackage.rabbitmq.lrpart_06.comments.entity.Comment;
import io.micrometer.core.instrument.MeterRegistry;
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

    private final MeterRegistry meterRegistry;

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
                                comment)
                        )
                        /* 메시지 플로를 추적하는 커스텀 매트릭스 추가 */
                        .then(Mono.just(comment)))
                        .log("commentService-publish")
                        .flatMap(comment -> {
                            /* 모든 코멘트와 함께 comments.produced 매트릭스를 증가시키는 데 사용 */
                            /* 각 매트릭스는 연관된 imageId로 태그 돼 있다. */
                            meterRegistry.counter("comments.produced", "imageId", comment.getId())
                                    .increment();
                            return Mono.just("redirect:/");
                        });
    }
}
