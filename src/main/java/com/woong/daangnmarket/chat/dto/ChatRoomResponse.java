
package com.woong.daangnmarket.chat.dto;

import com.woong.daangnmarket.chat.domain.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponse {
    private Long roomId;
    private Long postId;
    private Long sellerId;
    private Long buyerId;

    public ChatRoomResponse(ChatRoom entity) {
        this.roomId = entity.getId();
        this.postId = entity.getPostId();
        this.sellerId = entity.getSellerId();
        this.buyerId = entity.getBuyerId();
    }

    public ChatRoomResponse(Long roomId) {
        this.roomId = roomId;
    }
}
