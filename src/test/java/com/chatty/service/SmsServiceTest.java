package com.chatty.service;

import com.chatty.dto.sms.request.MessageDto;
import com.chatty.dto.sms.response.SmsResponseDto;
import com.chatty.service.sms.SmsService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmsServiceTest {

    @Autowired
    private SmsService smsService;

    @Test
    @DisplayName("SMS 문자 발송하기")
    void sendSMS() throws Exception {
        //given
        String number = "01077265108";
        String message = "인증번호 : 1234";
        MessageDto messageDto = MessageDto.builder()
                .to(number)
                .content(message).build();

        //when
        SmsResponseDto smsResponseDto = smsService.sendSms(messageDto);

        //then
        Assertions.assertThat(smsResponseDto.getStatusCode()).isEqualTo(202);

    }
}
