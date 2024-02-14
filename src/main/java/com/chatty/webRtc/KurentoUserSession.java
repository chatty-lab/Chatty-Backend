package com.chatty.webRtc;

import com.google.gson.JsonObject;
import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
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
public class KurentoUserSession implements Closeable {

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

    public void receiveCallFrom(KurentoUserSession sender, String sdpOffer) throws IOException {

        // 유저가 방에 들어옴
        log.info("USER {}: connecting with {} in room {}", this.name, sender.getName(), this.roomName);

        // 들어온 유저가 Sdp 제안
        log.trace("USER {}: SdpOffer for {} is {}", this.name, sender.getName(), sdpOffer);

        final String ipSdpAnswer = this.getEndpointForUser(sender).processAnswer(sdpOffer);
        final JsonObject scParams = new JsonObject();
        scParams.addProperty("id","receiveVideoAnswer");
        scParams.addProperty("name", sender.getName());
        scParams.addProperty("sdpAnswer",ipSdpAnswer);

        log.trace("USER {}: SdpAnswer for {} is {}", this.name, sender.getName(), ipSdpAnswer);
    }

    private WebRtcEndpoint getEndpointForUser(final KurentoUserSession sender){
        if(sender.getName().equals(name)) {
            log.debug("PARTICIPANT {}: configuring loopback", this.name);
            return outgoingMedia;
        }

        log.debug("PARTICIPANT {}: receiving voice from {}", this.name, sender.getName());

        WebRtcEndpoint incomingMedia = this.incomingMedia.get(sender.getName());

        if(incomingMedia == null){
            log.debug("PARTICIPANT {}: creating new endpoint for {}", this.name, sender.getName());
        }

        incomingMedia = new WebRtcEndpoint.Builder(pipeline)
                .useDataChannels()
                .build();

        incomingMedia.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {
            @Override
            public void onEvent(IceCandidateFoundEvent event) {
                JsonObject response = new JsonObject();

                response.addProperty("id","iceCandidate");
                response.addProperty("name",sender.getName());
                response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));

                try {
                    synchronized (session) {
                        session.sendMessage(new TextMessage(response.toString()));
                    }
                } catch (IOException e) {
                    log.debug(e.getMessage());
                }
            }
        });

        this.incomingMedia.put(sender.getName(), incomingMedia);

        log.debug("PARTICIPANT {}: obtained endpoint for {}", this.name, sender.getName());

        sender.getOutgoingMedia().connect(incomingMedia);

        return incomingMedia;
    }

    public void cancelCallFrom(final KurentoUserSession sender) {
        this.cancelCallFrom(sender.getName());
    }

    public void cancelCallFrom(final String name){
        log.debug("PARTICIPANT {}: canceling video reception from {}", this.name, name);

        final WebRtcEndpoint incoming = incomingMedia.remove(name);

        log.debug("PARTICIPANT {}: removing endpoint for {}", this.name, name);

        if(Objects.nonNull(incoming)){
            incoming.release(new Continuation<Void>() {
                @Override
                public void onSuccess(Void result) throws Exception {
                    log.trace("PARTICIPANT {}: Released successfully incoming EP for {}",
                            KurentoUserSession.this.name, name);
                }

                @Override
                public void onError(Throwable cause) throws Exception {
                    log.warn("PARTICIPANT {}: Could not release incoming EP for {}", KurentoUserSession.this.name,
                            name);
                }
            });
        }
    }

    @Override
    public void close() throws IOException {
        log.debug("PARTICIPANT {}: Releasing resources", this.name);
        for (final String remoteParticipantName : incomingMedia.keySet()) {

            log.trace("PARTICIPANT {}: Released incoming EP for {}", this.name, remoteParticipantName);

            final WebRtcEndpoint ep = this.incomingMedia.get(remoteParticipantName);

            ep.release(new Continuation<Void>() {

                @Override
                public void onSuccess(Void result) throws Exception {
                    log.trace("PARTICIPANT {}: Released successfully incoming EP for {}",
                            KurentoUserSession.this.name, remoteParticipantName);
                }

                @Override
                public void onError(Throwable cause) throws Exception {
                    log.warn("PARTICIPANT {}: Could not release incoming EP for {}", KurentoUserSession.this.name,
                            remoteParticipantName);
                }
            });
        }

        outgoingMedia.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("PARTICIPANT {}: Released outgoing EP", KurentoUserSession.this.name);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("USER {}: Could not release outgoing EP", KurentoUserSession.this.name);
            }
        });
    }

    public void sendMessage(JsonObject message) throws IOException {
        log.debug("USER {}: Sending message {}", name, message);
        synchronized (session) {
            try {
                session.sendMessage(new TextMessage(message.toString()));
            } catch (Exception e) {
                message.addProperty("id", "ConnectionFail");
                message.addProperty("data", e.getMessage());
                this.sendMessage(message);
            }
        }
    }

    public void addCandidate(IceCandidate candidate, String name) {
        if (this.name.compareTo(name) == 0) {
            outgoingMedia.addIceCandidate(candidate);
        } else {
            WebRtcEndpoint webRtc = incomingMedia.get(name);
            if (webRtc != null) {
                webRtc.addIceCandidate(candidate);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null || !(obj instanceof KurentoUserSession)) {
            return false;
        }
        KurentoUserSession other = (KurentoUserSession) obj;
        boolean eq = name.equals(other.name);
        eq &= roomName.equals(other.roomName);
        return eq;
    }


    @Override
    public int hashCode() {
        int result = 1;
        result = 31 * result + name.hashCode();
        result = 31 * result + roomName.hashCode();
        return result;
    }
}
