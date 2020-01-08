package com.psawesome.basepackage.rabbitmq.lrpart_06.images.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.images.dto
 * author: PS
 * DATE: 2020-01-08 수요일 21:52
 *
 * ReadOnly
 */
@Data
public class Comment {

    @Id
    private String id;
    private String imageId;
    private String comment;
}
