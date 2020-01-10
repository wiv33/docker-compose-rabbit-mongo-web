package com.psawesome.basepackage.rabbitmq.lrpart_06.images.repo;

import com.psawesome.basepackage.rabbitmq.lrpart_06.images.entity.Image;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

/**
 * package: com.psawesome.basepackage.learningreactivefile.repo
 * author: PS
 * DATE: 2020-01-03 금요일 22:38
 */
@Component
public class InitData {

    @Bean
    CommandLineRunner init(MongoOperations operations) {
        return args -> {
            operations.dropCollection(Image.class);

            operations.insert(new Image("1", "docker-logo.jpeg"));
            operations.insert(new Image("2", "l-r-Flux.jpg"));
            operations.insert(new Image("3", "l-r-Mono.jpg"));
        };
    }
}
