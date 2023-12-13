package com.chatty.utils;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationNumberUtil {
    private static final String authenticationNumber = "1234";

    public boolean isMatchNumber(String number){
        return authenticationNumber.equals(number);
    }
}
