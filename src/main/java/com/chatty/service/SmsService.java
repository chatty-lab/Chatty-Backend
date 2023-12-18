package com.chatty.service;

import static com.chatty.utils.SmsUtils.makeSignature;

import com.chatty.dto.MessageDto;
import com.chatty.dto.request.NaverSmsRequestDto;
import com.chatty.dto.request.UserSmsRequestDto;
import com.chatty.dto.response.SmsResponseDto;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Slf4j
@Service
public class SmsService {

    private static final String PREFIX_NUMBER = "010";

    @Value("${naver-cloud-sms-access-key}")
    private String accessKey;

    @Value("${naver-cloud-sms-secret-key}")
    private String secretKey;

    @Value("${naver-cloud-sms-service-id}")
    private String serviceId;

    @Value("${naver-cloud-sms-sender-phone-number}")
    private String phone;

    private RedisTemplate<String, String> redisTemplateAuthNumber;

    @Autowired
    public SmsService(@Qualifier("redisTemplateAuthNumber") RedisTemplate<String, String> redisTemplateAuthenticationNumber){
        log.debug("[SmsRepository 주입] {}",redisTemplateAuthenticationNumber);
        this.redisTemplateAuthNumber = redisTemplateAuthenticationNumber;
    }

    public void save(String key, String authNumber) {
        try {
            ValueOperations<String, String> value = redisTemplateAuthNumber.opsForValue();
            value.set(key,authNumber);
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 데이터 저장 실패");
        }
    }

    public String findAuthNumberByMobileNumber(String mobileNumber) {
        try {
            ValueOperations<String, String> value = redisTemplateAuthNumber.opsForValue();
            return value.get(mobileNumber);
        }catch(Exception e) {
            log.error("[RedistTokenService/getRefreshTokenByUuid] 일치하는 refresh 토큰이 존재하지 않습니다.");
            return null;
        }
    }

    public void delete(String mobileNumber) {
        redisTemplateAuthNumber.delete(mobileNumber);
    }

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
            String key = userSmsRequestDto.getMobileNumber() + userSmsRequestDto.getUuid();
            save(key,authNumber);
            log.info("번호 인증 요청 정보 저장 완료");
            return authNumber;
        }catch (Exception e){
            log.error(e.getMessage());
        }

        return null;
    }

    public boolean checkAuthNumber(String number, String authNumber) {
        try {
            String auth = findAuthNumberByMobileNumber(number);
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

        return true;
    }
}
