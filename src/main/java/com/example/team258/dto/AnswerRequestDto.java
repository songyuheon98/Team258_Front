package com.example.team258.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerRequestDto {
    private Long surveyId;
    private Long answer;
}
