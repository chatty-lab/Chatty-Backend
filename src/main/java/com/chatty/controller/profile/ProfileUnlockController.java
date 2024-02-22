package com.chatty.controller.profile;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.profileUnlock.request.ProfileUnlockRequest;
import com.chatty.dto.profileUnlock.response.ProfileUnlockResponse;
import com.chatty.dto.user.response.UserProfileResponse;
import com.chatty.service.profileUnlock.ProfileUnlockService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class ProfileUnlockController {

    private final ProfileUnlockService profileUnlockService;

    @Operation(summary = "프로필 잠금 해제", description = "상대방 프로필 잠금을 해제합니다.")
    @PostMapping("/profile/{userId}")
    public ApiResponse<ProfileUnlockResponse> unlockProfile(@PathVariable final Long userId, Authentication authentication,
                                                             @Valid @RequestBody final ProfileUnlockRequest request) {
        return ApiResponse.ok(profileUnlockService.unlockProfile(userId, authentication.getName(), request, LocalDateTime.now()));
    }

    @Operation(summary = "상대방 프로필 조회", description = "상대방 프로필을 조회합니다. 잠금을 해제하였으면 true, 아니면 false")
    @GetMapping("/profile/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(@PathVariable final Long userId, Authentication authentication) {
        return ApiResponse.ok(profileUnlockService.getUserProfile(userId, authentication.getName()));
    }
}
