package com.psawesome.basepackage.rabbitmq.lrpart_06.images.controllers;

import com.psawesome.basepackage.rabbitmq.lrpart_06.images.entity.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.controller
 * author: PS
 * DATE: 2020-01-02 목요일 23:13
 */
@Slf4j
@RestController
public class ApiController {
    final String API_BASE_PATH = "/api";

    @GetMapping(API_BASE_PATH + "/images")
    public Flux<Image> images() {
        return Flux.just(new Image("1", "webFlux.jpg"), new Image("2", "Mono.jpg"),
                new Image("3", "Streams.jpg"),
                new Image("4", "Api.jpg"));
    }

    @PostMapping(API_BASE_PATH + "/images")
    public Mono<Void> create(@RequestBody Flux<Image> images) {
        return images.map(image -> {
            log.info("we will Save {} to a Reactive database soon!", image);
            return image;
        })
                .then();
    }
}
