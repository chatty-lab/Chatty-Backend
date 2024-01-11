package com.chatty.controller.match;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.match.request.MatchRequest;
import com.chatty.dto.match.response.MatchResponse;
import com.chatty.service.match.MatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MatchController {

    private final MatchService matchService;

    @PostMapping("/match")
    public ApiResponse<MatchResponse> match(@Valid @RequestBody MatchRequest request, Authentication authentication) throws Exception {
        return ApiResponse.ok(matchService.createMatch(authentication.getName(), request));
    }
}
