package com.chatty.service;

import com.chatty.dto.request.UserRequestDto;
import com.chatty.entity.Authority;
import com.chatty.entity.User;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.repository.UserRepository;
import com.chatty.utils.AuthenticationNumberUtil;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final AuthenticationNumberUtil authenticationNumberUtil;

    public Map<String,String> login(UserRequestDto userRequestDto) {

        log.info("[UserService/login] 로그인 시작");

        if(!authenticationNumberUtil.isMatchNumber(userRequestDto.getAuthenticationNumber())){
            log.error("인증 번호가 일치하지 않는다.");
            return null;
        }

        if(!isAlreadyExistedUser(userRequestDto.getMobileNumber())){
            log.error("존재 하지 않는 유저 입니다.");
            return null;
        }

        return jwtTokenProvider.createTokens(userRequestDto.getMobileNumber(), userRequestDto.getUuid());
    }

    public Map<String,String> join(UserRequestDto userRequestDto) {

        log.info("[UserService/join] 회원 가입 시작");
        if(isAlreadyExistedUser(userRequestDto.getMobileNumber())){
            log.error("이미 존재 하는 유저 입니다.");
            return null;
        }

        if(!authenticationNumberUtil.isMatchNumber(userRequestDto.getAuthenticationNumber())){
            log.error("인증 번호가 일치하지 않는다.");
            return null;
        }

        User user = User.builder()
                .mobileNumber(userRequestDto.getMobileNumber())
                .authority(Authority.USER)
                .uuid(userRequestDto.getUuid())
                .build();
        userRepository.save(user);
        log.info("[UserService/join] 회원 가입 완료");
        return jwtTokenProvider.createTokens(user.getMobileNumber(), user.getUuid());
    }

    private boolean isAlreadyExistedUser(String mobileNumber){
        return userRepository.existsUserByMobileNumber(mobileNumber);
    }
}
