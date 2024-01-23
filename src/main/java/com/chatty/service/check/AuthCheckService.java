package com.chatty.service.check;

import com.chatty.constants.Code;
import com.chatty.dto.check.request.CheckRequestDto;
import com.chatty.dto.check.request.ProblemRequestDto;
import com.chatty.dto.check.response.ProblemResponseDto;
import com.chatty.exception.CustomException;
import com.chatty.repository.user.UserRepository;
import com.chatty.utils.check.CheckUtils;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCheckService {

    private final UserRepository userRepository;

    public ProblemResponseDto createNicknameProblem(ProblemRequestDto problemRequestDto){
        String mobileNumber = problemRequestDto.getMobileNumber();
        String nickname = userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(
                Code.NOT_EXIST_USER)).getNickname();

        return ProblemResponseDto.of(CheckUtils.createNicknameProblem(nickname));
    }

    public ProblemResponseDto createBirthProblem(ProblemRequestDto problemRequestDto) {
        String mobileNumber = problemRequestDto.getMobileNumber();
        LocalDate birth = userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER)).getBirth();

        return ProblemResponseDto.of(CheckUtils.createBirthProblem(birth));
    }

    public void checkNickName(CheckRequestDto checkRequestDto) {
        String mobileNumber = checkRequestDto.getMobileNumber();
        String nickname = userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(
                Code.NOT_EXIST_USER)).getNickname();

        if(!nickname.equals(checkRequestDto.getAnswer())){
            throw new CustomException(Code.FAIL_AUTH_CHECK);
        }
    }

    public void checkBirth(CheckRequestDto checkRequestDto){
        String mobileNumber = checkRequestDto.getMobileNumber();
        String year = String.valueOf(userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER)).getBirth().getYear());

        if(!year.equals(checkRequestDto.getAnswer())){
            throw new CustomException(Code.FAIL_AUTH_CHECK);
        }
    }
}
