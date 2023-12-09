package com.chatty.service;

import com.chatty.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(UserDetailsService.class);
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userMobileNumber) throws UsernameNotFoundException {

        LOGGER.info("[loadUserByUsername] loadUserByUsername 수행. userMobileNumber : {}", userMobileNumber);

        return userRepository.getUserByMobileNumber(userMobileNumber);
    }
}
