package com.chatty.security;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MobileNumberProvider {

    private final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final UserDetailsService userDetailsService;

    public Authentication getAuthentication(String mobileNumber){
        LOGGER.info("[getAuthentication] 전화번호 인증 정보 조회 시작");
        UserDetails userDetails = userDetailsService.loadUserByUsername(mobileNumber);

        return new MobileNumberAuthenticationToken(userDetails,userDetails.getAuthorities());
    }

    public boolean validation(String number){
        String authenticationNumber = "0123";

        return number.equals(authenticationNumber);
    }
}
