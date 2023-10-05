package com.example.team258.answer;

import java.time.LocalDateTime;

public class AnswerResponseDto {
    private Long surveyId;
    private Long answer;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public AnswerResponseDto(Answer answer){
        this.surveyId = answer.getSurvey().getSurveyId();
        this.answer = answer.getAnswerNum();
        this.createdAt = answer.getCreatedAt();
        this.modifiedAt = answer.getModifiedAt();
    }

}
