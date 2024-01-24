package com.chatty.controller.check;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.check.request.CheckRequestDto;
import com.chatty.dto.check.request.CompleteRequestDto;
import com.chatty.dto.check.request.ProblemRequestDto;
import com.chatty.dto.check.response.CheckCompleteResponseDto;
import com.chatty.dto.check.response.ProblemResponseDto;
import com.chatty.service.check.AuthCheckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class AuthCheckController {

    private final AuthCheckService authCheckService;

    @GetMapping("/problem/nickname")
    public ApiResponse<ProblemResponseDto> getNicknameProblem(@Valid @RequestBody ProblemRequestDto problemRequestDto) {
        log.info("[AuthCheckController/checkBirth] 닉네임 문제 획득");
        return ApiResponse.ok(authCheckService.createNicknameProblem(problemRequestDto));
    }

    @PostMapping("/nickname")
    public ApiResponse<String> checkNickname(@Valid @RequestBody CheckRequestDto checkRequestDto) {
        log.info("[AuthCheckController/checkBirth] 닉네임 확인");
        authCheckService.checkNickName(checkRequestDto);
        return ApiResponse.ok("닉네임 확인을 성공했습니다.");
    }

    @GetMapping("/problem/birth")
    public ApiResponse<ProblemResponseDto> getBirthProblem(@Valid @RequestBody ProblemRequestDto problemRequestDto) {
        log.info("[AuthCheckController/checkBirth] 출생 년도 문제 획득");
        return ApiResponse.ok(authCheckService.createBirthProblem(problemRequestDto));
    }

    @PostMapping("/birth")
    public ApiResponse<String> checkBirth(@Valid @RequestBody CheckRequestDto checkRequestDto) {
        log.info("[AuthCheckController/checkBirth] 출생 연도 월일 확인");
        authCheckService.checkBirth(checkRequestDto);
        return ApiResponse.ok("출생 연도 확인을 성공했습니다.");
    }

    @PostMapping("/complete")
    public ApiResponse<CheckCompleteResponseDto> complete(@Valid @RequestBody CompleteRequestDto completeRequestDto) {
        log.info("[AuthCheckController/complete] 계정 확인 완료");
        return ApiResponse.ok(authCheckService.complete(completeRequestDto));
    }
}
