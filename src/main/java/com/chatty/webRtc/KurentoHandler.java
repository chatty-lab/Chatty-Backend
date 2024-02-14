package com.chatty.webRtc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
public class KurentoHandler extends TextWebSocketHandler {

    private static final Gson gson = new GsonBuilder().create();

    // 유저 등록
    private final KurentouserRegistry registry;
}
