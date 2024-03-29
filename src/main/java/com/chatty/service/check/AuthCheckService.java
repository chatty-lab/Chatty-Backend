package com.chatty.service.check;

import com.chatty.constants.Code;
import com.chatty.dto.check.request.CheckRequestDto;
import com.chatty.dto.check.request.CompleteRequestDto;
import com.chatty.dto.check.request.ProblemRequestDto;
import com.chatty.dto.check.response.CheckCompleteResponseDto;
import com.chatty.dto.check.response.ProblemResponseDto;
import com.chatty.entity.check.AuthCheck;
import com.chatty.entity.user.User;
import com.chatty.exception.CustomException;
import com.chatty.jwt.JwtTokenProvider;
import com.chatty.repository.check.AuthCheckRepository;
import com.chatty.repository.token.RefreshTokenRepository;
import com.chatty.repository.user.UserRepository;
import com.chatty.utils.check.CheckUtils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCheckService {

    private final String NICKNAME = "nickname";
    private final String BIRTH = "birth";

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthCheckRepository authCheckRepository;

    @Transactional
    public ProblemResponseDto createNicknameProblem(ProblemRequestDto problemRequestDto) {
        String mobileNumber = problemRequestDto.getMobileNumber();

        User user = userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(
                Code.NOT_EXIST_USER));
        String nickname = user.getNickname();

        AuthCheck authCheck = AuthCheck.of(user.getId(), false, false);
        authCheckRepository.save(authCheck);

        return ProblemResponseDto.of(CheckUtils.createNicknameProblem(nickname));
    }

    public ProblemResponseDto createBirthProblem(ProblemRequestDto problemRequestDto) {
        String mobileNumber = problemRequestDto.getMobileNumber();
        LocalDate birth = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER)).getBirth();

        return ProblemResponseDto.of(CheckUtils.createBirthProblem(birth));
    }

    @Transactional
    public void checkNickName(CheckRequestDto checkRequestDto) {
        String mobileNumber = checkRequestDto.getMobileNumber();
        String nickname = userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(
                Code.NOT_EXIST_USER)).getNickname();

        if (!nickname.equals(checkRequestDto.getAnswer())) {
            throw new CustomException(Code.FAIL_AUTH_CHECK);
        }

        updateCheck(mobileNumber, NICKNAME, true);
    }

    @Transactional
    public void checkBirth(CheckRequestDto checkRequestDto) {
        String mobileNumber = checkRequestDto.getMobileNumber();
        String year = String.valueOf(userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER)).getBirth().getYear());

        if (!year.equals(checkRequestDto.getAnswer())) {
            throw new CustomException(Code.FAIL_AUTH_CHECK);
        }

        updateCheck(mobileNumber, BIRTH, true);
    }

    private void updateCheck(String mobileNumber, String kind, Boolean value) {
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));
        AuthCheck authCheck = authCheckRepository.findAuthCheckByUserId(user.getId())
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_AUTHCHECK));

        if (NICKNAME.equals(kind)) {
            authCheck.updateCheckNickname(value);
            return;
        }

        authCheck.updateCheckBirth(value);
    }

    @Transactional
    public CheckCompleteResponseDto complete(CompleteRequestDto completeRequestDto) {
        String mobileNumber = completeRequestDto.getMobileNumber();
        String deviceId = completeRequestDto.getDeviceId();
        User user = userRepository.findUserByMobileNumber(mobileNumber)
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER));

        log.info("모든 계정확인 질문 답변을 다 했는지 확인합니다.");
        AuthCheck authCheck = authCheckRepository.findAuthCheckByUserId(user.getId())
                .orElseThrow(() -> new CustomException(Code.NOT_EXIST_AUTHCHECK));

        if (!(authCheck.getCheckBirth() && authCheck.getCheckNickname())) {
            throw new CustomException(Code.NOT_CHECK_ALL_QUESTION);
        }

        log.info("deviceId를 업데이트합니다.");
        user.updateDeviceToken(deviceId);

        log.info("토큰을 생성합니다.");
        String accessToken = jwtTokenProvider.createAccessToken(mobileNumber, deviceId);
        String refreshToken = jwtTokenProvider.createRefreshToken(mobileNumber, deviceId);

        log.info("refreshToken을 저장합니다.");
        refreshTokenRepository.save(deviceId, refreshToken);

        log.info("AuthCheck 데이터 삭제");
        authCheckRepository.deleteAuthCheckByUserId(user.getId());

        return CheckCompleteResponseDto.of(accessToken, refreshToken);
    }
}
