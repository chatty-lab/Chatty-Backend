package com.chatty.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Gender {

    MALE("남"), FEMALE("여"), ALL("남/여");

    private final String gender;

    @JsonCreator
    public static Gender validateGender(String inputValue) {
        return Stream.of(Gender.values())
                .filter(gender -> gender.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
