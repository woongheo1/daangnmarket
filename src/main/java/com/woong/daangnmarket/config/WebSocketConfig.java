package com.woong.daangnmarket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // STOMP 기반 WebSocket 메시지 브로커 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // 클라이언트에서 ws://localhost:8080/ws 로 연결
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*"); // CORS 허용
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 구독(Subscribe) prefix → /topic
        registry.enableSimpleBroker("/topic");

        // 발행(Publish) prefix → /app
        registry.setApplicationDestinationPrefixes("/app");
    }
}
