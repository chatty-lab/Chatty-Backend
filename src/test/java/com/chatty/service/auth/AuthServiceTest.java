package com.chatty.service.auth;

import com.chatty.dto.auth.request.CheckTokenDto;
import com.chatty.validator.TokenValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private TokenValidator tokenValidator;

    @Test
    @DisplayName("accessToken을 검증한다.")
    void checkAccessToken() throws Exception{
        //given
        CheckTokenDto checkTokenDto = CheckTokenDto.builder()
                .accessToken("abcdefg").build();

        //when

        //then
    }
}
