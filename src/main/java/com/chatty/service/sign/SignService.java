package com.chatty.service.sign;

import com.chatty.common.CommonResponse;
import com.chatty.domain.user.User;
import com.chatty.domain.dto.sign.SignInResultDto;
import com.chatty.domain.dto.sign.SignUpResultDto;
import com.chatty.repository.UserRepository;
import com.chatty.security.JwtTokenProvider;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService {

    private final Logger LOGGER = LoggerFactory.getLogger(SignService.class);

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    private static final String ROLE_ADMIN = "ROLE_ADMIN";
    private static final String ROLE_USER = "ROLE_USER";

    public SignUpResultDto signUp(String mobileNumber, String name, List<String> role, String nickname) {

        LOGGER.info("[getSignUpResult] 회원 가입 정보 전달");
        User user = checkUser(mobileNumber, name, role, nickname);
        User savedUser = userRepository.save(user);
        LOGGER.info("[getSignUpResult] userEntity 값이 들어왔는지 확인 후 결과값 주입");
        SignUpResultDto signUpResultDto = checkResult(new SignInResultDto(), savedUser);
        signUpResultDto.setToken(jwtTokenProvider.createToken(mobileNumber,role));

        return signUpResultDto;
    }

    public SignInResultDto signIn(String mobileNumber, String password) throws RuntimeException {
        LOGGER.info("[getSignInResult] signDataHandler로 회원 정보 요청");
        User user = userRepository.getUserByMobileNumber(mobileNumber);
        LOGGER.info("[getSignInResult] mobileNumber : {}", mobileNumber);

        LOGGER.info("[getSignInResult] 패스워드 비교 수행");
        if(!passwordEncoder.matches(password,user.getPassword())){
            throw new RuntimeException();
        }

        LOGGER.info("[getSignInResult] 패스워드 일치");

        LOGGER.info("[getSignInResult] SignInResultDto 객체 생성");
        SignInResultDto signInResultDto = SignInResultDto.builder()
                .token(jwtTokenProvider.createToken(user.getMobileNumber(),user.getRoles()))
                .build();

        LOGGER.info("[getSignInResult] SignInResultDto 객체에 값 주입");
        return (SignInResultDto) setSuccessResult(signInResultDto);
    }
    private SignUpResultDto checkResult(SignUpResultDto result, User savedUser){
        if(!savedUser.getName().isEmpty()){
            LOGGER.info("[getSignUpResult] 정상 처리 완료");
            return setSuccessResult(result);
        }

        LOGGER.info("[getSignUpResult] 정상 처리 실패");
        return setFailResult(result);
    }

    private SignUpResultDto setSuccessResult(SignUpResultDto result){
        result.setSuccess(true);
        result.setCode(CommonResponse.SUCCESS.getCode());
        result.setMessage(CommonResponse.SUCCESS.getMessage());

        return result;
    }

    private SignUpResultDto setFailResult(SignUpResultDto result){
        result.setSuccess(false);
        result.setCode(CommonResponse.FAIL.getCode());
        result.setMessage(CommonResponse.FAIL.getMessage());

        return result;
    }

    private User checkUser(String mobileNumber, String name, List<String> role, String nickname){
        if(role.contains("ADMIN")){
            return makeUser(mobileNumber,name,ROLE_ADMIN, nickname);
        }

        return makeUser(mobileNumber, name, ROLE_USER, nickname);
    }

    private User makeUser(String mobileNumber, String name, String role, String nickname){
        return User.builder()
                .mobileNumber(mobileNumber)
                .nickName(nickname)
                .name(name)
                .roles(Collections.singletonList(role))
                .build();
    }

}
