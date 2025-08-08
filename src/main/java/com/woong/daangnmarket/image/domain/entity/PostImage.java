package com.woong.daangnmarket.image.domain.entity;

import com.woong.daangnmarket.post.domain.entity.Post;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", length = 300, nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostImage(String imageUrl, Post post) {
        this.imageUrl = imageUrl;
        this.post = post;
    }

    public void setPost(Post post) {
        this.post = post;
     }
}

