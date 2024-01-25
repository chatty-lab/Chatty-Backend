package com.chatty.dto.check.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProblemResponseDto {
    private List<String> problem;

    public static ProblemResponseDto of(List<String> problem){
        return ProblemResponseDto.builder().problem(problem).build();
    }
}
