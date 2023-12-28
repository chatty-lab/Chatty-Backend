package com.chatty.dto.user.request;

import com.chatty.entity.user.Coordinate;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCoordinateRequest {

    @NotNull(message = "좌표는 필수로 입력해야 합니다.")
    private Coordinate coordinate;

    @Builder
    public UserCoordinateRequest(final Coordinate coordinate) {
        this.coordinate = coordinate;
    }
}
