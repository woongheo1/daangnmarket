package com.woong.daangnmarket.chat.controller;

import com.woong.daangnmarket.chat.dto.ChatRoomRequest;
import com.woong.daangnmarket.chat.dto.ChatRoomResponse;
import com.woong.daangnmarket.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/rooms")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    // 채팅방 생성 (기존 방 있으면 반환)
    @PostMapping
    public ResponseEntity<ChatRoomResponse> createOrGetChatRoom(@RequestBody ChatRoomRequest request) {
        ChatRoomResponse response = chatRoomService.createOrGetChatRoom(request);
        return ResponseEntity.ok(response);
    }

    // 채팅방 조회
    @GetMapping("/{roomId}")
    public ResponseEntity<ChatRoomResponse> getChatRoom(@PathVariable("roomId") Long roomId) {
        ChatRoomResponse response = chatRoomService.findRoomById(roomId);
        return ResponseEntity.ok(response);
    }
}
