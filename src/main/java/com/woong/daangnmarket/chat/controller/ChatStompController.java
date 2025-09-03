package com.woong.daangnmarket.chat.controller;


import com.woong.daangnmarket.chat.domain.ChatMessage;
import com.woong.daangnmarket.chat.dto.ChatMessageDto;
import com.woong.daangnmarket.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatStompController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto dto) {
         chatMessageService.saveMessage(dto);

    }
}

