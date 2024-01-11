package com.chatty.entity.user;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

class GenderTest {

    @DisplayName("성별을 선택할 때, MALE과 FEMALE값을 입력하였는지 확인한다.")
    @CsvSource({"MALE, MALE", "FEMALE, FEMALE"})
    @ParameterizedTest
    void validateGender(String input, Gender expected) {
        // when
        Gender result = Gender.validateGender(input);

        // then
        assertThat(result).isEqualTo(expected);
    }

    @DisplayName("성별을 선택할 때, MALE과 FEMALE값이 아니면 null값을 반환한다.")
    @CsvSource({"남", "여", "ma", "fe"})
    @ParameterizedTest
    void validateGenderWithoutMaleAndFemale(String input) {
        // when
        Gender result = Gender.validateGender(input);

        // then
        assertThat(result).isEqualTo(null);
    }

}