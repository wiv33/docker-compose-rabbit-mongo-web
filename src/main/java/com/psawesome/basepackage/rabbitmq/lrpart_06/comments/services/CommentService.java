package com.psawesome.basepackage.rabbitmq.lrpart_06.comments.services;

import com.psawesome.basepackage.rabbitmq.lrpart_06.comments.entity.Comment;
import com.psawesome.basepackage.rabbitmq.lrpart_06.comments.repo.CommentWriterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.comments.service
 * author: PS
 * DATE: 2020-01-08 수요일 21:49
 */
@Service
@RequiredArgsConstructor
public class CommentService {

    private CommentWriterRepository repository;

    /*
        @RabbitListener : 메시지를 사용하는 방법을 등록하는 가장 쉬운 방법.
        @QueueBinding : 큐와 익스체인지를 즉시 선언하는 방법
                        선언된 메서드에 대한 익명 큐를 생성하고
                        learning-spring-boot 익스체인지에 바인딩한다.
     */
    @RabbitListener(bindings = @QueueBinding(
            /*
                save 메서드의 routing key는 contents.new 이며
                이는 learning-spring-boot 익스체인지에 게시된 메시지가
                이 메서드를 호출하게 한다는 것을 의미한다.
            */
            value = @Queue,
            exchange = @Exchange(value = "learning-spring-boot"),
            key = "comments.new"
    ))
    public void save(Comment newComment) {
        repository
                .save(newComment)
                .log("commentService-save")
                .subscribe();
    }

    /*
        스프링 부트의 자동 설정 정책으로
        AmqpAdmin 인스턴스가 애플리케이션 컨텍스트에 선언되어 있지 않아도 된다.

        직렬화 관련 내용
        커스텀 도메인 객체의 경우, 스프링 AMQP 메시지 컨버터와 같이 더 선호되는 솔루션이 있다.

        ==

        스프링 AMQP Message 객체를 직렬화/역직렬화하는 데 사용되는 AMQP의 MessageConverter 구현인
        Jackson2JsonMessageConverter를 생성했다.
        이 경우 jackson이 POJO를 JSON 문자열 변환에 사용된다.

        ==

        스프링 부트의 자동 설정 정책은 스프링 AMQP의 MessageConverter 인스턴스 구현을 찾아
        이전에 사용한 RabbitTemplate 과 코드에서 @RabbitListener 를 위치시킬 때 생성하는
        SimpleMessageListenerContainer 로 등록한다.
    */
    @Bean
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() { return new Jackson2JsonMessageConverter();}

    @Bean("setUpComment")
    @Profile("dev") /* 이런 위험한 실행은 dev 만 */
    CommandLineRunner setUp(MongoOperations operations) {
        return args -> operations.dropCollection(Comment.class);
    }
}
