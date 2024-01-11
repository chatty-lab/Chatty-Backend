package com.chatty.handler;
import com.chatty.validator.TokenValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final TokenValidator tokenValidator;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("Stomp Handler 실행");
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        log.info("accessor : {}",accessor);
        log.info("stompCommand : {}",accessor.getCommand());
        if(StompCommand.CONNECT.equals(accessor.getCommand()) || StompCommand.SEND.equals(accessor.getCommand())){ // 연결할때
            String accessToken = accessor.getFirstNativeHeader("Authorization");
            tokenValidator.validateAccessToken(accessToken);
            log.info("토큰 검증 완료");
        }
        log.info("message : {}",message);
        return message;
    }
}
