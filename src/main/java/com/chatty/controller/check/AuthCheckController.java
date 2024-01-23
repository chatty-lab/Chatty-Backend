package com.chatty.controller.check;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.check.request.CheckRequestDto;
import com.chatty.dto.check.response.ProblemResponseDto;
import com.chatty.service.check.AuthCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/check")
public class AuthCheckController {

    private final AuthCheckService authCheckService;

    @GetMapping("/problem/nickname")
    public ApiResponse<ProblemResponseDto> getNicknameProblem(@RequestBody CheckRequestDto checkRequestDto){
        return ApiResponse.ok(authCheckService.createNicknameProblem(checkRequestDto));
    }
}
