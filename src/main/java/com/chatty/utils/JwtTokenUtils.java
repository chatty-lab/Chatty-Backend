package com.chatty.utils;

import java.util.UUID;

public class JwtTokenUtils {

    public static String getAccessToken(String token){
        return token.split(" ")[1];
    }

    public static String getRefreshTokenRandomUuid(String mobileNumber){ // Redis의 저장되는 key
        StringBuilder sb = new StringBuilder();
        sb.append(mobileNumber);
        sb.append(UUID.randomUUID());

        return sb.toString();
    }
}
