package com.chatty.jwt;

import com.chatty.service.UserDetailsServiceImpl;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER_TYPE = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = resolveToken(request);

        if(!validateToken(token)){
            filterChain.doFilter(request,response);
            return;
        }

        String userMobileNumber = jwtTokenProvider.getMobileNumber(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userMobileNumber);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,
                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    private boolean validateToken(String token){
        if(!jwtTokenProvider.isExistToken(token)){ // 토큰 존재 여부
            return false;
        }

        if(!jwtTokenProvider.isRightFormat(token, BEARER_TYPE)){
            return false;
        }

        if(jwtTokenProvider.isExpiredToken(token)){ // 토큰이 만료된 경우
            return false;
        }

        return true;
    }

    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION);

        log.info("Header : {}", bearerToken);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)){
            return bearerToken;
        }

        return null;
    }
}
