package com.woong.daangnmarket.chat.service;

import com.woong.daangnmarket.chat.domain.ChatRoom;
import com.woong.daangnmarket.chat.dto.ChatRoomRequest;
import com.woong.daangnmarket.chat.dto.ChatRoomResponse;
import com.woong.daangnmarket.chat.respository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public ChatRoomResponse createOrGetChatRoom(ChatRoomRequest request) {
        // 기존 채팅방 찾기
        ChatRoom chatRoom = chatRoomRepository.findByPostIdAndBuyerIdAndSellerId(
                request.getPostId(), request.getBuyerId(), request.getSellerId());

        if (chatRoom != null) {
            return new ChatRoomResponse(chatRoom);
        }

        // 없으면 새로 생성
        ChatRoom newChatRoom = ChatRoom.builder()
                .postId(request.getPostId())
                .sellerId(request.getSellerId())
                .buyerId(request.getBuyerId())
                .build();

        ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);
        return new ChatRoomResponse(savedChatRoom);
    }

    @Transactional(readOnly = true)
    public ChatRoomResponse findRoomById(Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));
        return new ChatRoomResponse(chatRoom);
    }
}
