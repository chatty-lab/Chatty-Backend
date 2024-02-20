package com.chatty.config;

import com.chatty.service.match.MatchHandler;
import com.chatty.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket
public class WebSocketMatchConfig implements WebSocketConfigurer {

    private final MatchHandler matchHandler;
    private final TokenValidator tokenValidator;

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(matchHandler, "ws/match")
                .setAllowedOrigins("*")
                .addInterceptors(new WebSocketMatchInterceptor(tokenValidator));
    }
}
