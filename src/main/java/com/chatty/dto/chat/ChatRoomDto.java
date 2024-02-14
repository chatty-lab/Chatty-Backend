package com.chatty.dto.chat;

import com.chatty.constants.ChatType;
import jakarta.validation.constraints.NotNull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
    @NotNull
    private String roomId;
    private String roomName;
    private int userCount;
    private int maxUserCnt;

    private ChatType chatType;

    public ConcurrentMap<String, ?> userList = new ConcurrentHashMap<>();
}
