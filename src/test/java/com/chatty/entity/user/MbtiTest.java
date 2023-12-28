package com.chatty.entity.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class MbtiTest {

    @DisplayName("Mbti를 선택할 때, 올바른 Mbti를 선택했는지 확인한다.")
    @CsvSource({"ISTJ", "INTJ", "ESTJ", "ENTJ",
            "ISTP", "INTP", "ESTP", "ENTP",
            "ISFJ", "INFJ", "ESFJ", "ENFJ",
            "ISFP", "INFP", "ESFP", "ENFP"})
    @ParameterizedTest
    void validateMbti(String input) {
        // when
        Mbti result = Mbti.validateMbti(input);

        // then
        assertThat(result).isEqualTo(Mbti.valueOf(input));
    }

    @DisplayName("Mbti를 선택할 때, 정해진 Mbti값이 아니면 null값이 나와 예외가 발생한다.")
    @CsvSource({"ISFQ", "EQWE", "ESTX", "EHTJ"})
    @ParameterizedTest
    void validateMbtiWithoutEnumMbit(String input) {
        // when
        Mbti result = Mbti.validateMbti(input);

        // then
        assertThat(result).isNull();
    }
}