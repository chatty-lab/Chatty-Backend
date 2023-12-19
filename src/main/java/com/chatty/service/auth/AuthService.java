package com.chatty.service.auth;

import com.chatty.constants.ErrorCode;
import com.chatty.dto.auth.response.AuthResponseDto;
import com.chatty.exception.CustomException;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.repository.token.RefreshTokenRepository;
import com.chatty.service.user.UserDetailsServiceImpl;
import com.chatty.utils.JwtTokenUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final RefreshTokenRepository refreshTokenRepository;

    public AuthResponseDto reissueTokens(String accessToken, String refreshToken) {

        // access 토큰이 유효 한지 검사
        if(!validateAccessToken(accessToken)){
            log.error("[AuthService/reissueTokens] accessToken 유효성 검사 실패");
            throw new CustomException(ErrorCode.NOT_VALID_ACCESS_TOKEN);
        }

        // refresh 토큰이 유효 한지 검사
        if (!validateRefreshToken(refreshToken)) {
            log.error("[AuthService/reissueTokens] refreshToken 유효성 검사 실패");
            throw new CustomException(ErrorCode.NOT_VALID_REFRESH_TOKEN);
        }

        Map<String,String> tokens = createTokens(refreshToken);

        // refresh 토큰이 유효한 경우 토큰 재발급
        return AuthResponseDto.of(tokens.get(ACCESS_TOKEN), tokens.get(REFRESH_TOKEN));
    }

    private HashMap<String, String> createTokens(String refreshToken) {

        // access Token은 만료되었기 때문에 데이터 추출이
        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(jwtTokenProvider.getUuidByRefreshToken(refreshToken).split(" ")[0]);
        if (userDetails == null) {
            log.error("[AuthService/reissueTokens] 전달받은 accessToken의 정보를 가지는 사용자가 존재하지 않습니다.");
            throw new CustomException(ErrorCode.NOT_VALID_ACCESS_TOKEN);
        }

        log.info("[AuthService/createTokens] 새로운 토큰 발급 시작");
        // refreshToken은 Redis에 기존에 저장되어 있던 것 지우고 새로 발급
        refreshTokenRepository.delete(jwtTokenProvider.getUuidByRefreshToken(refreshToken));
        log.info("[AuthService/createTokens] Redis에 존재하는 기존 refresh Token 제거");

        String newAccessToken = jwtTokenProvider.createAccessToken(userDetails.getUsername(), userDetails.getPassword());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userDetails.getUsername());
        refreshTokenRepository.save(jwtTokenProvider.getUuidByRefreshToken(newRefreshToken), newRefreshToken);
        log.info("[AuthService/createToken] 새로 발급한 refresh Token redis 저장");

        return getTokens(newAccessToken, newRefreshToken);
    }

    private HashMap<String, String> getTokens(String newAccessToken, String newRefreshToken) {
        HashMap<String, String> newTokens = new HashMap<>();
        newTokens.put(ACCESS_TOKEN, newAccessToken);
        newTokens.put(REFRESH_TOKEN, newRefreshToken);
        return newTokens;
    }

    public boolean validateAccessToken(String accessToken){

        if(!jwtTokenProvider.isExistToken(accessToken)){
            log.error("accessToken이 존재하지 않습니다.");
            return false;
        }

        if(!jwtTokenProvider.isRightFormat(accessToken)){
            log.error("올바른 토큰의 형식을 입력해주세요.");
            return false;
        }

        if(!jwtTokenProvider.isValidToken(JwtTokenUtils.getAccessToken(accessToken))){
            log.error("유효하지 않은 accessToken 입니다.");
            return false;
        }

        if(!jwtTokenProvider.isExpiredToken(JwtTokenUtils.getAccessToken(accessToken))){
            log.error("accessToken이 만료되지 않았습니다.");
            return false;
        }
        return true;
    }

    public boolean validateRefreshToken(String refreshToken) {

        if (!jwtTokenProvider.isExistToken(refreshToken)) {
            log.error("refreshToken이 존재하지 않습니다.");
            return false;
        }

        if(!jwtTokenProvider.isValidToken(refreshToken)){
            log.error("유효하지 않은 토큰 입니다.");
            return false;
        }

        if (jwtTokenProvider.isExpiredToken(refreshToken)) {
            log.error("refreshToken이 만료 되었습니다.");
            // 만료된 경우 로그아웃 시키기
            return false;
        }

        if (!jwtTokenProvider.isEqualRedisRefresh(refreshToken, jwtTokenProvider.getUuidByRefreshToken(refreshToken))) {
            log.error("refreshToken이 DB에 저장된 refreshToken과 일치하지 않습니다.");
            return false;
        }

        return true;
    }
}
