package com.chatty.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KurentoUserRegistryService {

    private final ConcurrentHashMap<String, KurentoUser>
}
