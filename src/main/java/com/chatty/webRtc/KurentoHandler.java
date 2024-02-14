package com.chatty.webRtc;

import com.chatty.dto.chat.KurentoRoomDto;
import com.chatty.service.chat.KurentoManager;
import com.chatty.service.chat.KurentoUserRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
public class KurentoHandler extends TextWebSocketHandler {

    private static final Gson gson = new GsonBuilder().create();

    private final KurentoUserRegistry registry;

    private final KurentoManager roomManager;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        final KurentoUserSession user = registry.getBySession(session);

        if (user != null) {
            log.debug("Incoming message from user '{}': {}", user.getName(), jsonMessage);
        } else {
            log.debug("Incoming message from new user: {}", jsonMessage);
        }

        switch (jsonMessage.get("id").getAsString()) {
            case "joinRoom":
                joinRoom(jsonMessage, session);
                break;

            case "receiveVideoFrom":
                try {
                    final String senderName = jsonMessage.get("sender").getAsString();
                    final KurentoUserSession sender = registry.getByName(senderName);
                    final String sdpOffer = jsonMessage.get("sdpOffer").getAsString();
                    user.receiveCallFrom(sender, sdpOffer);
                } catch (Exception e){
                    connectException(user, e);
                }
                break;

            case "leaveRoom":
                leaveRoom(user);
                break;

            case "onIceCandidate":
                JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

                if (user != null) {
                    IceCandidate cand = new IceCandidate(candidate.get("candidate").getAsString(),
                            candidate.get("sdpMid").getAsString(), candidate.get("sdpMLineIndex").getAsInt());
                    user.addCandidate(cand, jsonMessage.get("name").getAsString());
                }
                break;

            default:
                break;
        }
    }

    private void joinRoom(JsonObject params, WebSocketSession session) throws IOException {

        final String roomName = params.get("room").getAsString();
        final String name = params.get("name").getAsString();
        log.info("PARTICIPANT {}: trying to join room {}", name, roomName);

        KurentoRoomDto room = roomManager.getRoom(roomName);

        final KurentoUserSession user = room.join(name, session);

        registry.register(user);
    }

    private void leaveRoom(KurentoUserSession user) throws IOException {
        if (Objects.isNull(user)) {
            return;
        }

        final KurentoRoomDto room = roomManager.getRoom(user.getRoomName());

        if (!room.getParticipants().contains(user)) {
            return;
        }

        room.leave(user);

        room.setUserCount(room.getUserCount()-1);
    }

    private void connectException(KurentoUserSession user, Exception e) throws IOException {
        JsonObject message = new JsonObject();
        message.addProperty("id", "ConnectionFail");
        message.addProperty("data", e.getMessage());

        user.sendMessage(message);

    }
}
