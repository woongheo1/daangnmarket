package com.woong.daangnmarket.chat.service;

import com.woong.daangnmarket.chat.domain.ChatMessage;
import com.woong.daangnmarket.chat.domain.ChatRoom;
import com.woong.daangnmarket.chat.dto.ChatMessageDto;
import com.woong.daangnmarket.chat.respository.ChatMessageRepository;
import com.woong.daangnmarket.chat.respository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatMessage saveMessage(ChatMessageDto dto) {
        ChatRoom room = chatRoomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방이 존재하지 않습니다."));

        ChatMessage message = ChatMessage.builder()
                .chatRoom(room)
                .senderId(dto.getSenderId())
                .message(dto.getMessage())
                .sentAt(LocalDateTime.now())
                .build();

        ChatMessage savedMessage = chatMessageRepository.save(message);

        // 저장된 메시지를 구독자에게 브로드캐스트
        messagingTemplate.convertAndSend(
                "/topic/chat/" + dto.getRoomId(),
                dto
        );

        return savedMessage;
    }
}
