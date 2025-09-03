package com.woong.daangnmarket.chat.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDto {

    private long roomId;
    private long senderId;     // 발신자 (username, userId 등)
    private String message;    // 메시지 내용
    private String timestamp;  // 보낸 시간
}
