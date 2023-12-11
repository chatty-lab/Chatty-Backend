package com.chatty.controller;

import com.chatty.domain.dto.sign.SignInResultDto;
import com.chatty.domain.dto.sign.SignUpResultDto;
import com.chatty.service.sign.SignService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sign-api")
@RequiredArgsConstructor
public class SignController {
    private final Logger LOGGER = LoggerFactory.getLogger(SignController.class);
    private final SignService signService;

    @PostMapping(value = "/sign-in")
    public SignInResultDto sigIn(@RequestBody Map<String,String> map) {
        String mobileNumber = map.get("mobileNumber");
        String password = map.get("password");

        LOGGER.info("[siginIn] 로그인을 시도하고 있습니다. mobileNumber : {}, pw : ****", mobileNumber);
        SignInResultDto signInResultDto = signService.signIn(mobileNumber,password);

        if(signInResultDto.getCode() == 0){
            LOGGER.info("[signIn] 정상적으로 로그인되었습니다. mobileNumber : {}, token : {}", mobileNumber, signInResultDto.getToken());
        }

        return signInResultDto;
    }

    @PostMapping(value = "/sign-up")
    public SignUpResultDto signUp(@RequestBody Map<String, String> map){
        String mobileNumber = map.get("mobileNumber");
        String nickname = map.get("nickName");
        String name = map.get("name");
        List<String> role = Arrays.stream(map.get("role").split(",")).toList();

        LOGGER.info("[siginUp] 회원가입을 수행합니다. mobileNumber : {}, pw : ****, name : {}, nickname : {}, role : {}", mobileNumber, name, nickname, role);

        SignUpResultDto signUpResultDto = signService.signUp(mobileNumber,name,role, nickname);

        LOGGER.info("[siginUp] 회원가입을 완료했습니다.");

        return signUpResultDto;
    }

    @GetMapping(value = "/exception")
    public void exceptionTest() throws RuntimeException {
        throw  new RuntimeException("접근 금지");
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Map<String,String>> ExceptionHandler(RuntimeException e){
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        LOGGER.error("ExceptionHandler 호출, {},{}", e.getCause(), e.getMessage());

        Map<String, String> map = new HashMap<>();
        map.put("error type", httpStatus.getReasonPhrase());
        map.put("code","400");
        map.put("message","에러 발생");

        return new ResponseEntity<>(map, responseHeaders, httpStatus);
    }
}
