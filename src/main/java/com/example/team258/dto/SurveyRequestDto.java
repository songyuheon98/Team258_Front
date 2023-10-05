package com.example.team258.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SurveyRequestDto {
    private String question;
    private String choices;
    private Long maxChoice;
    private LocalDateTime deadline;
}
