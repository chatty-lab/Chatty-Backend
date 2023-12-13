package com.chatty.service;

import com.chatty.dto.UserRequestDto;
import com.chatty.entity.Authority;
import com.chatty.entity.User;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.repository.UserRepository;
import com.chatty.utils.AuthenticationNumberUtil;
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

    public String login(UserRequestDto userRequestDto) {

        log.info("[UserService/login] 로그인 시작");
        if(authenticationNumberUtil.isMatchNumber(userRequestDto.getAuthenticationNumber())){
            String mobileNumber = userRequestDto.getMobileNumber();
            String uuid = userRequestDto.getUuid();
            return jwtTokenProvider.createToken(mobileNumber, uuid);
        }

       return null;
    }

    public String join(UserRequestDto userJoinRequestDto) {

        log.info("[UserService/join] 회원 가입 시작");
        if(isAlreadyExistedUser(userJoinRequestDto.getMobileNumber())){
            log.error("이미 존재 하는 유저 입니다.");
            return null;
        }

        if(authenticationNumberUtil.isMatchNumber(userJoinRequestDto.getAuthenticationNumber())){
            User user = User.builder()
                    .mobileNumber(userJoinRequestDto.getMobileNumber())
                    .authority(Authority.USER)
                    .uuid(userJoinRequestDto.getUuid())
                    .build();
            userRepository.save(user);
            log.info("[UserService/join] 회원 가입 완료");
            return jwtTokenProvider.createToken(user.getMobileNumber(), user.getUuid());
        }

        return null;
    }

    private boolean isAlreadyExistedUser(String mobileNumber){
        return userRepository.existsUserByMobileNumber(mobileNumber);
    }
}
