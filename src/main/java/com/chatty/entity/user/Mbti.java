package com.chatty.entity.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public enum Mbti {
    ISTJ, INTJ, ESTJ, ENTJ,
    ISTP, INTP, ESTP, ENTP,
    ISFJ, INFJ, ESFJ, ENFJ,
    ISFP, INFP, ESFP, ENFP;


    @JsonCreator
    public static Mbti validateMbti(String inputValue) {
        return Stream.of(Mbti.values())
                .filter(mbti -> mbti.toString().equals(inputValue.toUpperCase()))
                .findFirst()
                .orElse(null);
    }
}
