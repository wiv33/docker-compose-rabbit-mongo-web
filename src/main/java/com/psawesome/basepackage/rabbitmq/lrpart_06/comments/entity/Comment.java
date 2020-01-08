package com.psawesome.basepackage.rabbitmq.lrpart_06.comments.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.comments
 * author: PS
 * DATE: 2020-01-08 수요일 21:47
 */
@Document
@Data
public class Comment {

    @Id
    private String id;
    private String imageId;
    private String comment;

}
