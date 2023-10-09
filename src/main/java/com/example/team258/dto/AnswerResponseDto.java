package com.example.team258.dto;

import com.example.team258.entity.Answer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnswerResponseDto {
    private Long answerId;
    private Long surveyId;
    private Long answer;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public AnswerResponseDto(Answer answer){
        this.answerId = answer.getAnswerId();
        this.surveyId = answer.getSurvey().getSurveyId();
        this.answer = answer.getAnswerNum();
        this.createdAt = answer.getCreatedAt();
        this.modifiedAt = answer.getModifiedAt();
    }

}
