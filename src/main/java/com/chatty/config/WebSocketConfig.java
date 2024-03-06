package com.chatty.config;

import com.chatty.handler.ChatHandler;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer, WebSocketConfigurer {

    private final TokenValidator tokenValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final ChatHandler chatHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws")
                .addInterceptors()
                .setAllowedOriginPatterns("*");

        registry.addEndpoint("/signaling")
                .addInterceptors()
                .setAllowedOrigins("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지를 구독하는 요청 url
        registry.enableSimpleBroker("/sub");

        // 메시지를 발행하는 요청 url
        registry.setApplicationDestinationPrefixes("/pub"); // Controller 객체의 MessageMapping 메서드 라우팅
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(chatHandler,"/ws")
                .addHandler(chatHandler,"/signaling")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketMatchInterceptor(tokenValidator, jwtTokenProvider));
    }
}
