package com.woong.daangnmarket.chat.respository;

import com.woong.daangnmarket.chat.domain.ChatMessage;
import com.woong.daangnmarket.chat.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    // 특정 채팅방의 모든 메시지 조회
    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}
