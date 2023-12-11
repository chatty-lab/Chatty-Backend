package com.chatty.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

public class MobileNumberAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final Logger LOGGER =  LoggerFactory.getLogger(MobileNumberAuthenticationFilter.class);
    private final MobileNumberProvider mobileNumberProvider;

    public MobileNumberAuthenticationFilter(String defaultFilterProcessesUrl, MobileNumberProvider mobileNumberProvider) {
        super(defaultFilterProcessesUrl);
        this.mobileNumberProvider = mobileNumberProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {

        String mobileNumber = request.getParameter("mobileNumber");

        LOGGER.info("[attemptAuthentication] 전화번호 유효성 값 체크");
        if(!mobileNumberProvider.validation(mobileNumber)){
            throw new AuthenticationServiceException("전화번호 유효성 값 체크 실패");
        }

        Authentication authentication = mobileNumberProvider.getAuthentication(mobileNumber);
        LOGGER.info("[attemptAuthentication] 전화번호 유효성 값 체크 완료");

        return getAuthenticationManager().authenticate(authentication);
    }
}
