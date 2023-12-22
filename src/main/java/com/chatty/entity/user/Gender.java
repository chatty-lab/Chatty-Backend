package com.chatty.entity.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public enum Gender {

    MALE("남"), FEMALE("여");

    private final String gender;

}
