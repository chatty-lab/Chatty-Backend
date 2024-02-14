package com.chatty.config;

import com.chatty.service.chat.KurentoManager;
import com.chatty.service.chat.KurentoUserRegistry;
import com.chatty.webRtc.KurentoHandler;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.kurento.client.KurentoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebRtcConfig implements WebSocketConfigurer {

    @Value("${kms.url]")
    private String kmsUrl;

    private final KurentoUserRegistry registry;

    private final KurentoManager roomManager;

    @Bean
    public KurentoHandler kurentoHandler(){
        return new KurentoHandler(registry, roomManager);
    }

    @Bean
    public KurentoClient kurentoClient(){
        String envKmsUrl = System.getenv("KMS_URL");
        if(Objects.isNull(envKmsUrl) || envKmsUrl.isEmpty()){
            return KurentoClient.create(kmsUrl);
        }

        return KurentoClient.create(envKmsUrl);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(kurentoHandler(), "/signal")
                .setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(32768);
        container.setMaxBinaryMessageBufferSize(32768);
        return container;
    }
}
