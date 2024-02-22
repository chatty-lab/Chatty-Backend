package com.chatty.controller.profile;

import com.chatty.dto.ApiResponse;
import com.chatty.dto.profileUnlock.request.ProfileUnlockRequest;
import com.chatty.dto.profileUnlock.response.ProfileUnlockResponse;
import com.chatty.dto.user.response.UserProfileResponse;
import com.chatty.service.profileUnlock.ProfileUnlockService;
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

    @PostMapping("/profile/{userId}")
    public ApiResponse<ProfileUnlockResponse> unlockProfile(@PathVariable final Long userId, Authentication authentication,
                                                             @Valid @RequestBody final ProfileUnlockRequest request) {
        return ApiResponse.ok(profileUnlockService.unlockProfile(userId, authentication.getName(), request, LocalDateTime.now()));
    }

    @GetMapping("/profile/{userId}")
    public ApiResponse<UserProfileResponse> getUserProfile(@PathVariable final Long userId, Authentication authentication) {
        return ApiResponse.ok(profileUnlockService.getUserProfile(userId, authentication.getName()));
    }
}
