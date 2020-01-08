package com.psawesome.basepackage.rabbitmq.lrpart_06.images.entity;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * package: com.psawesome.basepackage.rabbitmq.lrpart_06.entity
 * author: PS
 * DATE: 2020-01-02 목요일 21:58
 */
@Data
@RequiredArgsConstructor
@Document
public class Image {

    @Id
    private final String id;
    private final String name;

}
