package com.example.team258.answer;

import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import com.example.team258.repository.AnswerRepository;
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

    @Transactional
    public ResponseEntity<String> updateAnswer(AnswerRequestDto requestDto,Long answerId, User user) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(()->new NullPointerException("예외가 발생하였습니다."));
        if (answer.getUser().getUserId() != user.getUserId()){
            throw new IllegalArgumentException("예외가 발생하였습니다.");
        }
        answer.update(requestDto.getAnswer());
        return ResponseEntity.ok("수정이 완료되었습니다.");
    }

    public ResponseEntity<String> deleteAnswer(Long answerId, User user) {
        Answer answer = answerRepository.findById(answerId).orElseThrow(()->new NullPointerException("예외가 발생하였습니다."));
        if (answer.getUser().getUserId() != user.getUserId()){
            throw new IllegalArgumentException("예외가 발생하였습니다.");
        }
        answerRepository.delete(answer);
        return ResponseEntity.ok("삭제가 완료되었습니다.");
    }

}
