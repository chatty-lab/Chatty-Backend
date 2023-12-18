package com.chatty.service;

import static com.chatty.utils.SmsUtils.makeSignature;

import com.chatty.dto.MessageDto;
import com.chatty.dto.request.NaverSmsRequestDto;
import com.chatty.dto.request.UserSmsRequestDto;
import com.chatty.dto.response.SmsResponseDto;
import com.chatty.repository.AuthNumberRepository;
import com.chatty.utils.SmsUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {

    private static final String PREFIX_NUMBER = "010";
    private static final String REGEX = "^[0-9]*$";
    private static final int MOBILE_NUMBER_LENGTH = 11;

    @Value("${naver-cloud-sms-access-key}")
    private String accessKey;

    @Value("${naver-cloud-sms-secret-key}")
    private String secretKey;

    @Value("${naver-cloud-sms-service-id}")
    private String serviceId;

    @Value("${naver-cloud-sms-sender-phone-number}")
    private String phone;

    private final AuthNumberRepository authNumberRepository;

    public SmsResponseDto sendSms(MessageDto messageDto) throws JsonProcessingException, RestClientException, URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
        Long time = System.currentTimeMillis();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(accessKey,serviceId,secretKey,time));

        List<MessageDto> messages = new ArrayList<>();
        messages.add(messageDto);

        NaverSmsRequestDto request = NaverSmsRequestDto.builder()
                .type("SMS")
                .contentType("COMM")
                .countryCode("82")
                .from(phone)
                .content(messageDto.getContent())
                .messages(messages)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(request);
        HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        SmsResponseDto response = restTemplate.postForObject(new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ serviceId +"/messages"), httpBody, SmsResponseDto.class);

        return response;
    }

    public String saveSms(UserSmsRequestDto userSmsRequestDto) throws Exception {
        if(!validateNumber(userSmsRequestDto.getMobileNumber())){
            throw new Exception("올바르지 않은 번호 형식");
        }

        try {
            String authNumber = SmsUtils.generateNumber();
            String key = SmsUtils.makeKey(userSmsRequestDto.getMobileNumber(),userSmsRequestDto.getUuid());
            authNumberRepository.save(key,authNumber);
            log.info("번호 인증 요청 정보 저장 완료 : {}", authNumber);
            return authNumber;
        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    public boolean checkAuthNumber(String key, String authNumber) {
        try {
            String auth = authNumberRepository.findAuthNumber(key);
            log.info("확인이 필요한 인증번호 : {}",auth);
            if(auth.equals(authNumber)){
                return true;
            }
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return false;
    }

    public boolean validateNumber(String number){
        if(!number.startsWith(PREFIX_NUMBER)){
            return false;
        }

        if(!number.matches(REGEX)){
            return false;
        }

        if(number.length() != MOBILE_NUMBER_LENGTH){
            return false;
        }

        return true;
    }
}
