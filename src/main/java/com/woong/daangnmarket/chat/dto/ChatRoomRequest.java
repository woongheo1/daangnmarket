
package com.woong.daangnmarket.chat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomRequest {
    private Long postId;
    private Long sellerId;
    private Long buyerId;
}
