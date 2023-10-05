package com.example.team258.service;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.entity.Answer;
import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import com.example.team258.repository.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final SurveyRepository surveyRepository;

    public ResponseEntity<String> createAnswer(AnswerRequestDto requestDto, User user) {
        Survey survey = surveyRepository.findById(requestDto.getSurveyId());
        Answer answer = new Answer(requestDto.getAnswer(), user,survey);
        Answer savedAnswer = answerRepository.save(answer);
        return ResponseEntity.ok("작성이 완료되었습니다.");
    }

    public List<AnswerResponseDto> getAnswers(User user) {
        List<Answer> answerList = answerRepository.findAllByUser(user);
        return answerList.stream().map(i-> new AnswerResponseDto(i)).collect(Collectors.toList());
    }
}
