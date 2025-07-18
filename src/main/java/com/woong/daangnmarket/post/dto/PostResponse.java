package com.woong.daangnmarket.post.dto;

import com.woong.daangnmarket.post.domain.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;
    private final Integer price;
    private final String region;
    private final String status;
    private final String imageUrl;
    private final Long memberId;
    private final Long categoryId;
    private final LocalDateTime createdAt;
    private final Double latitude;
    private final Double longitude;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.price = post.getPrice();
        this.region = post.getRegion();
        this.status = post.getStatus();
        this.imageUrl = post.getImageUrl();
        this.memberId = post.getMember().getId();
        this.categoryId = post.getCategory() != null ? post.getCategory().getId() : null;
        this.createdAt = post.getCreatedAt();

        if (post.getLocation() != null) {
            this.latitude = post.getLocation().getLatitude();
            this.longitude = post.getLocation().getLongitude();
        } else {
            this.latitude = null;
            this.longitude = null;
        }
    }

    // 정적 팩토리 메서드
    public static PostResponse from(Post post) {
        return new PostResponse(post);
    }
}
