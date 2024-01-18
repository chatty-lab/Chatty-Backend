package com.chatty.validator;

import com.chatty.constants.Code;
import com.chatty.exception.CustomException;
import com.chatty.jwt.JwtTokenProvider;

import com.chatty.utils.jwt.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class TokenValidator {

    private final JwtTokenProvider jwtTokenProvider;

    public void  validateAccessToken(String accessToken){

        log.info("[JwtTokenFilter] accessToken 유효 여부 확인");
        log.info("입력된 토큰 : {}",accessToken);

        if(!jwtTokenProvider.isExistToken(accessToken)){ // 토큰 존재 여부
            log.error("accessToken 토큰이 존재하지 않습니다.");
            throw new CustomException(Code.INVALID_TOKEN);
        }

        if(!jwtTokenProvider.isRightFormat(accessToken)){ // 토큰 형식 여부
            log.error("올바른 토큰의 형식을 입력해주세요.");
            throw new CustomException(Code.INVALID_TOKEN);
        }

        if(!jwtTokenProvider.isValidToken(JwtTokenUtils.getAccessToken(accessToken))){
            log.error("유효한 accessToken이 아닙니다.");
            throw new CustomException(Code.INVALID_TOKEN);
        }

        if(jwtTokenProvider.isExpiredToken(JwtTokenUtils.getAccessToken(accessToken))){ // 토큰이 만료된 경우
            log.error("accessToken 만료");
            throw new CustomException(Code.EXPIRED_ACCESS_TOKEN);
        }

    }

    public void validateRefreshToken(String refreshToken) {

        if (!jwtTokenProvider.isExistToken(refreshToken)) {
            log.error("refreshToken이 존재하지 않습니다.");
            throw new CustomException(Code.INVALID_TOKEN);
        }

        if(!jwtTokenProvider.isValidToken(refreshToken)){
            log.error("유효하지 않은 토큰 입니다.");
            throw new CustomException(Code.INVALID_TOKEN);
        }

        if (jwtTokenProvider.isExpiredToken(refreshToken)) {
            log.error("refreshToken이 만료 되었습니다.");
            throw new CustomException(Code.EXPIRED_REFRESH_TOKEN);
        }

        if (!jwtTokenProvider.isEqualRedisRefresh(refreshToken, jwtTokenProvider.getDeviceIdByRefreshToken(refreshToken))) {
            log.error("refreshToken이 DB에 저장된 refreshToken과 일치하지 않습니다.");
            throw new CustomException(Code.INVALID_TOKEN);
        }
    }
}
