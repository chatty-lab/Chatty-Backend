package com.chatty.config;

import java.util.concurrent.ConcurrentHashMap;

public class WebSocketConnectionManager {

    private static ConcurrentHashMap<String, String> userSessions = new ConcurrentHashMap<>();

    public static boolean addConnection(String mobileNumber, String sessionId) {
        return userSessions.putIfAbsent(mobileNumber, sessionId) == null;
    }

    public static void removeConnection(String mobileNumber) {
        userSessions.remove(mobileNumber);
    }

    public static boolean isConnected(String mobileNumber) {
        return userSessions.containsKey(mobileNumber);
    }

    public static String getConnected(String mobileNumber) {
        return userSessions.get(mobileNumber);
    }
}
