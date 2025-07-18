package com.woong.daangnmarket.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequest {

    private String title;         // 게시글 제목
    private String content;       // 게시글 내용
    private Integer price;        // 상품 가격
    private String region;        // 지역 (예: 서울 송파구)
    private String status;        // 판매 상태 (SALE, RESERVED, SOLD)
    private String imageUrl;      // 대표 이미지 URL
    private Long memberId;        // 작성자 ID
    private Long categoryId;      // 카테고리 ID
    private Double latitude;    //위도
    private Double longitude;   //경도
}
