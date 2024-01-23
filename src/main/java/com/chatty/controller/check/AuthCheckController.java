package com.chatty.controller.check;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.check.response.ProblemResponseDto;
import com.chatty.service.check.AuthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class AuthCheckController {

    private final AuthCheckService authCheckService;

    @GetMapping("/problem/nickname")
    public ApiResponse<ProblemResponseDto> getNicknameProblem(final Authentication authentication){
        return ApiResponse.ok(authCheckService.createNicknameProblem(authentication.getName()));
    }

    @GetMapping("/problem/birth")
    public ApiResponse<ProblemResponseDto> getBirthProblem(final Authentication authentication){
        return ApiResponse.ok(authCheckService.createBirthProblem(authentication.getName()));
    }
}
