package com.chatty.service.check;

import com.chatty.constants.Code;
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

    public ProblemResponseDto createNicknameProblem(String mobileNumber){
        String nickname = userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(
                Code.NOT_EXIST_USER)).getNickname();

        return ProblemResponseDto.of(CheckUtils.createNicknameProblem(nickname));
    }

    public ProblemResponseDto createBirthProblem(String mobileNumber) {
        LocalDate birth = userRepository.findUserByMobileNumber(mobileNumber).orElseThrow(() -> new CustomException(Code.NOT_EXIST_USER)).getBirth();

        return ProblemResponseDto.of(CheckUtils.createBirthProblem(birth));
    }
}
