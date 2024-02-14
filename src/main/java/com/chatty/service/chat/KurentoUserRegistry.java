package com.chatty.service.chat;

import com.chatty.webRtc.KurentoUserSession;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

@Service
@RequiredArgsConstructor
public class KurentoUserRegistry {

    private final ConcurrentHashMap<String, KurentoUserSession> usersByName = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<String, KurentoUserSession> usersBySessionId = new ConcurrentHashMap<>();

    public void register(KurentoUserSession user) {
        usersByName.put(user.getName(), user);
        usersBySessionId.put(user.getSession().getId(),user);
    }

    public KurentoUserSession getByName(String name) {
        return usersByName.get(name);
    }

    public KurentoUserSession getBySession(WebSocketSession session){
        return usersBySessionId.get(session.getId());
    }

    public boolean exists(String name){
        return usersByName.keySet().contains(name);
    }

    public KurentoUserSession removeBySession(WebSocketSession session){
        final KurentoUserSession user = getBySession(session);
        if(Objects.nonNull(user)){
            usersByName.remove(user.getName());
            usersBySessionId.remove(session.getId());
        }
        return user;
    }
}
