package com.chatty.dto.chat;

import com.chatty.constants.ChatType;
import com.chatty.webRtc.KurentoUserSession;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import jakarta.annotation.PreDestroy;
import jakarta.validation.constraints.NotNull;
import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.Continuation;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class KurentoRoomDto extends ChatRoomDto implements Closeable {

    private KurentoClient kurento;

    // 미디어 파이프라인
    private MediaPipeline pipeline;

    @NotNull
    private String roomId; // 채팅방 아이디
    private String roomName; // 채팅방 이름
    private int userCount; // 채팅방 인원수
    private int maxUserCnt; // 채팅방 최대 인원 제한

    private String roomPwd; // 채팅방 삭제시 필요한 pwd
    private boolean secretChk; // 채팅방 잠금 여부
    private ChatType chatType; //  채팅 타입 여부

    private ConcurrentMap<String, KurentoUserSession> participants;


    public void setRoomInfo(String roomId, String roomName, String roomPwd, boolean secure, int userCount, int maxUserCnt, ChatType chatType, KurentoClient kurento){
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomPwd = roomPwd;
        this.secretChk = secure;
        this.userCount = userCount;
        this.maxUserCnt = maxUserCnt;
        this.chatType = chatType;
        this.kurento = kurento;
        this.participants = (ConcurrentMap<String, KurentoUserSession>) this.userList;
    }

    @Override
    public String getRoomId() {
        return roomId;
    }

    public void createPipline(){
        this.pipeline = this.kurento.createMediaPipeline();
    }

    @PreDestroy
    private void shutdown() {
        this.close();
    }

    public Collection<KurentoUserSession> getParticipants() {
        return participants.values();
    }

    public KurentoUserSession getParticipant(String name) {
        return participants.get(name);
    }

    public KurentoUserSession join(String userName, WebSocketSession session) throws IOException {

        log.info("ROOM {}: adding participant {}", this.roomId, userName);

        final KurentoUserSession participant = new KurentoUserSession(userName, this.roomId, session, this.pipeline);

        joinRoom(participant);

        participants.put(participant.getName(), participant);

        sendParticipantNames(participant);

        userCount++;

        return participant;
    }

    public void leave(KurentoUserSession user) throws IOException {
        log.debug("PARTICIPANT {}: Leaving room {}", user.getName(), this.roomId);
        this.removeParticipant(user.getName());

        user.close();
    }

    private Collection<String> joinRoom(KurentoUserSession newParticipant) throws IOException {
        final JsonObject newParticipantMsg = new JsonObject();

        newParticipantMsg.addProperty("id", "newParticipantArrived");
        newParticipantMsg.addProperty("name", newParticipant.getName());

        final List<String> participantsList = new ArrayList<>(participants.values().size());

        log.debug("ROOM {}: 다른 참여자들에게 새로운 참여자가 들어왔음을 알림 {}", roomId,
                newParticipant.getName());

        for (final KurentoUserSession participant : participants.values()) {
            try {
                participant.sendMessage(newParticipantMsg);
            } catch (final IOException e) {
                log.error("ROOM {}: participant {} could not be notified", roomId, participant.getName(), e);
            }

            participantsList.add(participant.getName());
        }

        return participantsList;
    }

    private void removeParticipant(String name) throws IOException {

        participants.remove(name);

        log.debug("ROOM {}: notifying all users that {} is leaving the room", this.roomId, name);

        final List<String> unNotifiedParticipants = new ArrayList<>();

        final JsonObject participantLeftJson = new JsonObject();

        participantLeftJson.addProperty("id", "participantLeft");
        participantLeftJson.addProperty("name", name);

        for (final KurentoUserSession participant : participants.values()) {
            try {
                participant.cancelCallFrom(name);
                participant.sendMessage(participantLeftJson);

            } catch (final IOException e) {
                unNotifiedParticipants.add(participant.getName());
            }
        }

        if (!unNotifiedParticipants.isEmpty()) {
            log.debug("ROOM {}: The users {} could not be notified that {} left the room", this.roomId,
                    unNotifiedParticipants, name);
        }

    }

    public void sendParticipantNames(KurentoUserSession user) throws IOException {
        final JsonArray participantsArray = new JsonArray();

        for (final KurentoUserSession participant : this.getParticipants()) {
            if (!participant.equals(user)) {

                final JsonElement participantName = new JsonPrimitive(participant.getName());
                participantsArray.add(participantName);
            }
        }

        final JsonObject existingParticipantsMsg = new JsonObject();

        existingParticipantsMsg.addProperty("id", "existingParticipants");
        existingParticipantsMsg.add("data", participantsArray);
        log.debug("PARTICIPANT {}: sending a list of {} participants", user.getName(),
                participantsArray.size());

        user.sendMessage(existingParticipantsMsg);
    }

    @Override
    public void close() {
        for (final KurentoUserSession user : participants.values()) {
            try {
                user.close();
            } catch (IOException e) {
                log.debug("ROOM {}: Could not invoke close on participant {}", this.roomId, user.getName(), e);
            }
        }

        participants.clear();


        pipeline.release(new Continuation<Void>() {

            @Override
            public void onSuccess(Void result) throws Exception {
                log.trace("ROOM {}: Released Pipeline", KurentoRoomDto.this.roomId);
            }

            @Override
            public void onError(Throwable cause) throws Exception {
                log.warn("PARTICIPANT {}: Could not release Pipeline", KurentoRoomDto.this.roomId);
            }
        });

        log.debug("Room {} closed", this.roomId);
    }
}
