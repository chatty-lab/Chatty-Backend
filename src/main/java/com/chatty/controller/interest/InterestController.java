package com.chatty.controller.interest;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.interest.response.InterestResponse;
import com.chatty.service.interest.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class InterestController {

    private final InterestService interestService;

    @GetMapping("/api/v1/interests")
    public ApiResponse<List<InterestResponse>> getInterests() {
        return ApiResponse.ok(interestService.getInterests());
    }
}
