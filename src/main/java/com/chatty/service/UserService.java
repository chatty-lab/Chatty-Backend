package com.chatty.service;

import com.chatty.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;

    public String login(String mobileNumber, String uuid) {
        return jwtTokenProvider.createToken(mobileNumber, uuid);
    }
}
