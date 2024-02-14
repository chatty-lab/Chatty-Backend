package com.chatty.service.chat;

import com.chatty.dto.chat.ChatRoomDto;
import com.chatty.dto.chat.ChatRoomMap;
import com.chatty.dto.chat.KurentoRoomDto;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KurentoManager {

    private final ConcurrentMap<String, ChatRoomDto> rooms = ChatRoomMap.getInstance().getChatRooms();

    public KurentoRoomDto getRoom(String roomId){
        log.debug("Searching for room {}", roomId);

        KurentoRoomDto room = (KurentoRoomDto) rooms.get(roomId);

        return room;
    }

    public void removeRoom(KurentoRoomDto room) {
        this.rooms.remove(room.getRoomId());

        room.close();

        log.info("Room {} removed and closed", room.getRoomId());
    }
}
