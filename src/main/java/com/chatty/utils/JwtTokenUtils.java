package com.chatty.utils;

import java.util.UUID;

public class JwtTokenUtils {

    public static String getAccessToken(String token){
        return token.split(" ")[1];
    }

    public static String getRefreshTokenUuid(String mobileNumber, String uuid){ // Redis의 저장되는 key
        StringBuilder sb = new StringBuilder();
        sb.append(mobileNumber + " ");
        sb.append(uuid);

        return sb.toString();
    }
}
