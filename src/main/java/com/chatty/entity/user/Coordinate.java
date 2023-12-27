package com.chatty.entity.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Coordinate {

    private Double lat;
    private Double lng;

    @Builder
    public Coordinate(final Double lat, final Double lng) {
        this.lat = lat;
        this.lng = lng;
    }
}
