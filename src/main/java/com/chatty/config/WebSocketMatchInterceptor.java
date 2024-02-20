package com.chatty.config;

import com.chatty.dto.ApiResponse;
import com.chatty.exception.CustomException;
import com.chatty.validator.TokenValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.net.InetSocketAddress;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class WebSocketMatchInterceptor implements HandshakeInterceptor {

    private final TokenValidator tokenValidator;

    @Override
    public boolean beforeHandshake(final ServerHttpRequest request, final ServerHttpResponse response, final WebSocketHandler wsHandler, final Map<String, Object> attributes) throws Exception {
        String token = request.getHeaders().getFirst("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            try {
                tokenValidator.validateAccessToken(token);
            } catch (CustomException e){
                log.error("valid 에러 코드 발생");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(final ServerHttpRequest request, final ServerHttpResponse response, final WebSocketHandler wsHandler, final Exception exception) {
    }
}
