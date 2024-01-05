package com.chatty.service.auth;

import com.chatty.constants.Code;
import com.chatty.dto.auth.request.AuthRequestDto;
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

    public AuthResponseDto reissueTokens(AuthRequestDto authRequestDto) {

        String refreshToken = jwtTokenProvider.resolvRefreshToken(authRequestDto);

        validateRefreshToken(refreshToken);

        Map<String,String> tokens = createTokens(refreshToken);

        return AuthResponseDto.of(tokens.get(ACCESS_TOKEN), tokens.get(REFRESH_TOKEN));
    }

    private HashMap<String, String> createTokens(String refreshToken) {

        UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(jwtTokenProvider.getUuidByRefreshToken(refreshToken).split(" ")[0]);

        log.info("[AuthService/createTokens] 새로운 토큰 발급 시작");
        // refreshToken은 Redis에 기존에 저장되어 있던 것 지우고 새로 발급
        refreshTokenRepository.delete(jwtTokenProvider.getUuidByRefreshToken(refreshToken));
        log.info("[AuthService/createTokens] Redis에 존재하는 기존 refresh Token 제거");

        String newAccessToken = jwtTokenProvider.createAccessToken(userDetails.getUsername(), userDetails.getPassword());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userDetails.getUsername(), userDetails.getPassword());
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

    public void validateRefreshToken(String refreshToken) {

        if (!jwtTokenProvider.isExistToken(refreshToken)) {
            log.error("refreshToken이 존재하지 않습니다.");
            throw new CustomException(Code.INVALID_REFRESH_TOKEN);
        }

        if(!jwtTokenProvider.isValidToken(refreshToken)){
            log.error("유효하지 않은 토큰 입니다.");
            throw new CustomException(Code.INVALID_REFRESH_TOKEN);
        }

        if (jwtTokenProvider.isExpiredToken(refreshToken)) {
            log.error("refreshToken이 만료 되었습니다.");
            throw new CustomException(Code.EXPIRED_REFRESH_TOKEN);
        }

        if (!jwtTokenProvider.isEqualRedisRefresh(refreshToken, jwtTokenProvider.getUuidByRefreshToken(refreshToken))) {
            log.error("refreshToken이 DB에 저장된 refreshToken과 일치하지 않습니다.");
            throw new CustomException(Code.INVALID_REFRESH_TOKEN);
        }
    }
}
