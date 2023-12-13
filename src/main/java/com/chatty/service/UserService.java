package com.chatty.service;

import com.chatty.dto.UserJoinRequestDto;
import com.chatty.dto.UserLoginRequestDto;
import com.chatty.entity.Authority;
import com.chatty.entity.User;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.repository.UserRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public String login(UserLoginRequestDto userLoginRequestDto) {
        String mobileNumber = userLoginRequestDto.getMobileNumber();
        String uuid = userLoginRequestDto.getUuid();
        return jwtTokenProvider.createToken(mobileNumber, uuid);
    }

    public String join(UserJoinRequestDto userJoinRequestDto) {

        log.info("회원가입 시작");

        if(isAlreadyExistedUser(userJoinRequestDto.getMobileNumber())){
            log.error("이미 존재하는 유저입니다.");
            return null;
        }

        User user = User.builder()
                .mobileNumber(userJoinRequestDto.getMobileNumber())
                .address(userJoinRequestDto.getAddress())
                .gender(userJoinRequestDto.getGender())
                .birth(LocalDate.parse(userJoinRequestDto.getBirth()))
                .mbti(userJoinRequestDto.getMbti())
                .nickname(userJoinRequestDto.getNickname())
                .authority(Authority.valueOf(userJoinRequestDto.getAuthority()))
                .location(new Point(userJoinRequestDto.getLatitude(), userJoinRequestDto.getLongitude()))
                .username(userJoinRequestDto.getUsername())
                .uuid(userJoinRequestDto.getUuid())
                .build();

        userRepository.save(user);

        log.info("회원가입 완료");

        return jwtTokenProvider.createToken(user.getMobileNumber(), user.getUuid());
    }

    private boolean isAlreadyExistedUser(String mobileNumber){
        return userRepository.existsUserByMobileNumber(mobileNumber);
    }
}
