package com.chatty.jwt;

import com.chatty.constants.Code;
import com.chatty.exception.CustomException;
import com.chatty.service.user.UserDetailsServiceImpl;
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

        validateAccessToken(accessToken);

        String userMobileNumber = jwtTokenProvider.getMobileNumber(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userMobileNumber);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private void validateAccessToken(String accessToken){

        log.info("[JwtTokenFilter] accessToken 유효 여부 확인");

        if(!jwtTokenProvider.isExistToken(accessToken)){ // 토큰 존재 여부
            log.error("accessToken 토큰이 존재하지 않습니다.");
            throw new CustomException(Code.INVALID_ACCESS_TOKEN);
        }

        if(!jwtTokenProvider.isRightFormat(accessToken)){ // 토큰 형식 여부
            log.error("올바른 토큰의 형식을 입력해주세요.");
            throw new CustomException(Code.INVALID_ACCESS_TOKEN);
        }

        if(!jwtTokenProvider.isValidToken(accessToken)){
            log.error("유효한 accessToken이 아닙니다.");
            throw new CustomException(Code.INVALID_ACCESS_TOKEN);
        }

        if(jwtTokenProvider.isExpiredToken(JwtTokenUtils.getAccessToken(accessToken))){ // 토큰이 만료된 경우
            log.error("accessToken 만료");
            throw new CustomException(Code.NOT_EXPIRED_ACCESS_TOKEN);
        }

    }
}
