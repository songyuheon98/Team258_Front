package com.example.team258.service;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.entity.Answer;
import com.example.team258.entity.MessageDto;
import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import com.example.team258.repository.AnswerRepository;
import com.example.team258.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {
    private final AnswerRepository answerRepository;
    private final SurveyRepository surveyRepository;

    public MessageDto createAnswer(AnswerRequestDto requestDto, User user) {
        Survey survey = surveyRepository.findById(requestDto.getSurveyId()).orElseThrow(()->new NullPointerException("예외가 발생하였습니다."));
        Answer answer = new Answer(requestDto.getAnswer(), user,survey);
        Answer savedAnswer = answerRepository.save(answer);
        MessageDto message = new MessageDto("작성이 완료되었습니다.");
        return message;
    }

    public List<AnswerResponseDto> getAnswers(User user) {
        List<Answer> answerList = answerRepository.findAllByUser(user);
        return answerList.stream().map(i-> new AnswerResponseDto(i)).collect(Collectors.toList());
    }

    @Transactional
    public MessageDto updateAnswer(AnswerRequestDto requestDto,Long answerId, User user) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(()->new NullPointerException("예외가 발생하였습니다."));
        if (answer.getUser().getUserId() != user.getUserId()){
            throw new IllegalArgumentException("예외가 발생하였습니다.");
        }
        answer.update(requestDto.getAnswer());
        MessageDto message = new MessageDto("수정이 완료되었습니다.");
        return message;
    }

    public MessageDto deleteAnswer(Long answerId, User user) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(()->new NullPointerException("예외가 발생하였습니다."));
        if (answer.getUser().getUserId() != user.getUserId()){
            throw new IllegalArgumentException("예외가 발생하였습니다.");
        }
        answerRepository.delete(answer);
        MessageDto message = new MessageDto("삭제가 완료되었습니다.");
        return message;
    }

}
