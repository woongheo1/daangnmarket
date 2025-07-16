package com.woong.daangnmarket.post.domain.entity;


import com.woong.daangnmarket.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 게시글 고유 ID

    @Column(nullable = false, length = 255)
    private String title; // 게시글 제목

    @Lob
    @Column(nullable = false)
    private String content; // 게시글 내용

    private Integer price; // 상품 가격 (NULL 가능)

    @Column(nullable = false, length = 100)
    private String region; // 게시글 지역

    @Column(nullable = false, length = 20)
    private String status; // 판매 상태 (SALE, RESERVED, SOLD)

    @Column(name = "image_url", length = 300)
    private String imageUrl; // 대표 이미지 URL (NULL 가능)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member; // 작성자 (회원) FK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리 FK (NULL 가능)

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 생성 일자

    @Column(name = "IS_REMOVED")
    private Boolean removed = false;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public Post(String title, String content, Integer price, String region,
                String status, String imageUrl, Member member, Category category) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.region = region;
        this.status = status;
        this.imageUrl = imageUrl;
        this.member = member;
        this.category = category;
    }

    /**
     * 게시글을 소프트 삭제 처리하는 메서드입니다.
     * 실제로 데이터베이스에서 행(row)을 삭제하지 않고,
     * 삭제 여부를 나타내는 removed 필드를 true로 변경하여
     * 해당 게시글이 삭제된 상태임을 표시합니다.
     * 소프트 삭제를 위해서는 게시글 조회 시 removed가 false인 데이터만 조회하도록
     * 쿼리에서 조건을 추가해야 합니다.
     */
    public void removePost() {
        this.removed = true;
    }

}

