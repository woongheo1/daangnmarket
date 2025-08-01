package com.woong.daangnmarket.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostUpdateRequest {

    private String title;
    private String content;
    private Integer price;
    private String region;
    private String status;
    private String imageUrl;
    private Long categoryId;
    private Double latitude;
    private Double longitude;
}