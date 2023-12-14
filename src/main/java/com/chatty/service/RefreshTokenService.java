package com.chatty.service;

import com.chatty.entity.RefreshToken;
import com.chatty.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public RefreshToken saveRefreshToken(String refreshToken, String uuid) {
        return refreshTokenRepository.save(RefreshToken.builder()
                .uuid(uuid)
                .refreshToken(refreshToken)
                .build()
        );
    }

    @Transactional
    public void removeRefreshToken(String uuid){
        refreshTokenRepository.findRefreshTokenByUuid(uuid)
                .ifPresent(refreshTokenRepository::delete);
    }
}
