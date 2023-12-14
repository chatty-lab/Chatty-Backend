package com.chatty.jwt;

import com.chatty.service.UserDetailsServiceImpl;
import com.chatty.utils.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtTokenProvider.resolveAccessToken(request);
        String refreshToken = jwtTokenProvider.resolvRefreshToken(request);

        if(!validateAccessToken(accessToken)){
            log.error("AccessToken 오류");
            filterChain.doFilter(request,response);
            return;
        }

        if(!validateRefreshToken(refreshToken)){
            log.error("RefreshToken 오류");
            filterChain.doFilter(request,response);
            return;
        }

        String userMobileNumber = jwtTokenProvider.getMobileNumber(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userMobileNumber);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean validateRefreshToken(String refreshToken){

        log.info("refreshToken : {}",refreshToken);

        if(!jwtTokenProvider.isExistToken(refreshToken)){
            log.error("refreshToken이 존재 하지 안습니다.");
            return false;
        }

        if(!jwtTokenProvider.isValidToken(refreshToken)){
            log.error("유효한 refreshToken이 아닙니다.");
            return false;
        }

        // 리프레시 토큰이 redis에 저장된 토큰과 일치
        if(!jwtTokenProvider.isEqualRedisRefresh(refreshToken, jwtTokenProvider.getRefreshTokenUuid(refreshToken))){
            log.error("refreshToken이 일치하지 않습니다.");
            return false;
        }

        if(jwtTokenProvider.isExpiredToken(refreshToken)){
            log.error("refreshToken 만료");
            return false;
        }

        return true;
    }

    private boolean validateAccessToken(String accessToken){

        log.info("[JwtTokenFilter] accessToken 유효 여부 확인");

        if(!jwtTokenProvider.isExistToken(accessToken)){ // 토큰 존재 여부
            log.error("accessToken 토큰이 존재하지 않습니다.");
            return false;
        }

        if(!jwtTokenProvider.isRightFormat(accessToken)){ // 토큰 형식 여부
            log.error("올바른 토큰의 형식을 입력해주세요.");
            return false;
        }

        if(!jwtTokenProvider.isValidToken(accessToken)){
            log.error("유효한 accessToken이 아닙니다.");
            return false;
        }

        if(jwtTokenProvider.isExpiredToken(JwtTokenUtils.getAccessToken(accessToken))){ // 토큰이 만료된 경우
            log.error("accessToken 만료");
            return false;
        }

        return true;
    }
}
