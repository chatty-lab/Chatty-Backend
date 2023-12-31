package com.chatty.service.user;

import com.chatty.constants.Code;
import com.chatty.dto.user.request.*;
import com.chatty.dto.user.response.UserResponse;
import com.chatty.dto.user.response.UserResponseDto;
import com.chatty.constants.Authority;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.repository.token.RefreshTokenRepository;
import com.chatty.repository.user.UserRepository;
import com.chatty.service.sms.SmsService;
import com.chatty.utils.JwtTokenUtils;
import com.chatty.utils.SmsUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SmsService smsService;

    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    public UserResponseDto login(UserRequestDto userRequestDto) {

        log.info("[UserService/login] 로그인 시작");

        String key = SmsUtils.makeKey(userRequestDto.getMobileNumber(), userRequestDto.getUuid());
        String authNumber = userRequestDto.getAuthenticationNumber();

        if(!isAlreadyExistedUser(userRequestDto.getMobileNumber())){
            log.error("존재 하지 않는 유저 입니다.");
            throw new CustomException(Code.NOT_EXIST_USER);
        }

        if(!smsService.checkAuthNumber(key,authNumber)){
            log.error("인증 번호가 일치하지 않는다.");
            throw new CustomException(Code.INVALID_AUTH_NUMBER);
        }

        deleteToken(JwtTokenUtils.getRefreshTokenUuid(userRequestDto.getMobileNumber(),userRequestDto.getUuid()));
        Map<String,String> tokens = createTokens(userRequestDto.getMobileNumber(), userRequestDto.getUuid());
        return UserResponseDto.of(tokens.get(ACCESS_TOKEN), tokens.get(REFRESH_TOKEN));
    }

    public UserResponseDto join(UserRequestDto userRequestDto) {

        log.info("[UserService/join] 회원 가입 시작");
        String key = SmsUtils.makeKey(userRequestDto.getMobileNumber(),userRequestDto.getUuid());

        if(isAlreadyExistedUser(userRequestDto.getMobileNumber())){
            log.error("이미 존재 하는 유저 입니다.");
            throw new CustomException(Code.ALREADY_EXIST_USER);
        }

//        if(!smsService.checkAuthNumber(key,userRequestDto.getAuthenticationNumber())){
//            log.error("인증 번호가 일치하지 않는다.");
//            throw new CustomException(Code.INVALID_AUTH_NUMBER);
//        }

        User user = User.builder()
                .mobileNumber(userRequestDto.getMobileNumber())
                .authority(Authority.ANONYMOUS)
                .uuid(userRequestDto.getUuid())
                .build();

        userRepository.save(user);
        log.info("[UserService/join] 회원 가입 완료");

        Map<String,String> tokens = createTokens(userRequestDto.getMobileNumber(), userRequestDto.getUuid());
        return UserResponseDto.of(tokens.get(ACCESS_TOKEN), tokens.get(REFRESH_TOKEN));
    }

    private Map<String,String> createTokens(String mobileNumber, String uuid){

        log.info("[UserService/createTokens] AccessToken, RefreshToken 생성");
        Map<String,String> tokens = new HashMap<>();
        String accessToken = jwtTokenProvider.createAccessToken(mobileNumber,uuid);
        String refreshToken = jwtTokenProvider.createRefreshToken(mobileNumber, uuid);
        log.info("[UserService/createTokens] 생성한 accessToken : {}",accessToken);
        log.info("[UserService/createTokens] 생성한 refreshToken : {}",refreshToken);
        tokens.put(ACCESS_TOKEN, accessToken);
        tokens.put(REFRESH_TOKEN, refreshToken);

        log.info("[UserService/createTokens] RefreshToken Redis 저장");
        refreshTokenRepository.save(jwtTokenProvider.getUuidByRefreshToken(refreshToken),refreshToken);

        return tokens;
    }

    @Transactional
    public UserResponse joinComplete(final String mobileNumber, final UserJoinRequest request) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        user.joinComplete(request.toEntity());

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateNickname(final String mobileNumber, final UserNicknameRequest request) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        validateDuplicateNickname(request);

        user.updateNickname(request.getNickname());

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateGender(final String mobileNumber, final UserGenderRequest request) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        user.updateGender(request.getGender());

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateBirth(final String mobileNumber, final UserBirthRequest request) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        user.updateBirth(request.getBirth());

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateMbti(final String mobileNumber, final UserMbtiRequest request) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        user.updateMbti(request.getMbti());

        return UserResponse.of(user);
    }

    @Transactional
    public UserResponse updateCoordinate(final String mobileNumber, final UserCoordinateRequest request) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        user.updateCoordinate(request.getCoordinate());

        return UserResponse.of(user);
    }

    private void validateDuplicateNickname(final UserNicknameRequest request) {
        userRepository.findByNickname(request.getNickname())
                .ifPresent(findUser -> {
                    throw new CustomException(Code.ALREADY_EXIST_NICKNAME);
                });
    }

    private void deleteToken(String uuid){
        refreshTokenRepository.delete(uuid);
    }

    private boolean isAlreadyExistedUser(String mobileNumber){
        log.info("[UserService/isAlreadyExistedUser] 이미 가입한 유저인지 확인");
        return userRepository.existsUserByMobileNumber(mobileNumber);
    }

    public User validateExistUser(long userId) {
        log.info("유저가 유효한지 검사");
        return userRepository.findUserById(userId).orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));
    }
}
