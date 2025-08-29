package com.woong.daangnmarket.chat.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 ID (어떤 상품 채팅인지 구분)
    private Long postId;

    // 판매자 ID
    private Long sellerId;

    // 구매자 ID
    private Long buyerId;
}
