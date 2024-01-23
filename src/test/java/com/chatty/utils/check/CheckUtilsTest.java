package com.chatty.utils.check;

import java.time.LocalDate;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CheckUtilsTest {

    @Test
    @DisplayName("랜덤 년도 생성")
    void getRandomYear() throws Exception{
        //given
        LocalDate birth = LocalDate.of(1999,01,16);
        //when,then
        List<String> problems = CheckUtils.createBirthProblem(birth);

        System.out.println(problems);

        Assertions.assertThat(problems.size()).isEqualTo(5);
    }
}
