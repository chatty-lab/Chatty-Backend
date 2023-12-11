package com.chatty.security;

import java.util.Collection;
import javax.security.auth.Subject;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class MobileNumberAuthenticationToken extends AbstractAuthenticationToken {

    private final Object principal;
    private final Object credentials;

    public MobileNumberAuthenticationToken(String phoneNumber){ // 인증 요청
        super(null);
        principal = phoneNumber;
        credentials = "";
        setAuthenticated(false);
    }

    public MobileNumberAuthenticationToken(UserDetails userDetails, Collection<? extends GrantedAuthority> authorities) { // 인증 요청 완료후
        super(authorities);
        principal = userDetails;
        credentials = "";
        super.setAuthenticated(true); // must use super, as we override
    }

    @Override
    public Object getCredentials() { return credentials;}

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}
