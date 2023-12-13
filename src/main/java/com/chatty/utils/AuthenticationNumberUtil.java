package com.chatty.utils;

import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationNumberUtil {
    private static final String authenticationNumber = "123456";

    public boolean isMatchNumber(String number){
        return authenticationNumber.equals(number);
    }

    public String generateNumber(){
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            sb.append(new Random().nextInt(10));
        }

        return sb.toString();
    }
}
