package com.woong.daangnmarket.chat.respository;

import com.woong.daangnmarket.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    // 필요 시: 특정 게시글+구매자+판매자로 채팅방 찾기
    ChatRoom findByPostIdAndBuyerIdAndSellerId(Long postId, Long buyerId, Long sellerId);
}