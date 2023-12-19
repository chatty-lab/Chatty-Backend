package com.chatty.service.user;

import com.chatty.entity.user.User;
import com.chatty.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userMobileNumber) throws UsernameNotFoundException {

        User user = userRepository.findUserByMobileNumber(userMobileNumber).orElseThrow(() -> new UsernameNotFoundException("유저가 존재하지 않습니다."));

        return user;
    }
}
