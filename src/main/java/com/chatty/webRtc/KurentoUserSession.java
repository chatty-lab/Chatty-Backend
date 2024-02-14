package com.chatty.webRtc;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.jsonrpc.JsonUtils;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.kurento.client.*;

@Slf4j
@RequiredArgsConstructor
@Getter
public class KurentoUserSession {

    private final String name;
    private final WebSocketSession session;

    private final MediaPipeline pipeline;

    private final String roomName;

    private final WebRtcEndpoint outgoingMedia;

    private final ConcurrentMap<String, WebRtcEndpoint> incomingMedia = new ConcurrentHashMap<>();

    public KurentoUserSession(String name, String roomName, WebSocketSession session, MediaPipeline mediaPipeline) {
        this.pipeline = mediaPipeline;
        this.name = name;
        this.session = session;
        this.roomName = roomName;

        this.outgoingMedia = new WebRtcEndpoint.Builder(pipeline)
                .useDataChannels()
                .build();

        this.outgoingMedia.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {
            @Override
            public void onEvent(IceCandidateFoundEvent event) {
                JsonObject response = new JsonObject();

                // id: iceCandidate, id는 ice 후보자 선정
                response.addProperty("id", "iceCandidate");

                // name: 유저명
                response.addProperty("name",name);

                response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));

                try {
                    synchronized (session){
                        session.sendMessage(new TextMessage(response.toString()));
                    }
                }catch(IOException e) {
                    log.debug(e.getMessage());
                }
            }
        });
    }


}
