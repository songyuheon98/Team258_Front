package com.example.team258.dto;

import com.example.team258.entity.Survey;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyResponseDto {
    private Long surveyId;
    private String question;
    private String choices;

    public SurveyResponseDto(Survey survey) {
        this.surveyId = survey.getSurveyId();
        this.question = survey.getQuestion();
        this.choices = survey.getChoices();
    }

}
