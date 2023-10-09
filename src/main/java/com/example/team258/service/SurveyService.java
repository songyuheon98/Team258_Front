package com.example.team258.service;

import com.example.team258.dto.SurveyRequestDto;
import com.example.team258.dto.SurveyResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.SurveyRepository;
import com.example.team258.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final UserRepository userRepository;

    @Transactional
    public SurveyResponseDto createSurvey(SurveyRequestDto requestDto, User user) {
        Survey survey = new Survey(requestDto, user);
        surveyRepository.save(survey);
        User savedUser = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("(임시) 일치하는 유저 없음"));
        savedUser.addSurvey(survey);
        return new SurveyResponseDto(survey);
    }

    @Transactional
    public SurveyResponseDto getSurvey(Long surveyId) {
        Survey survey = getSurveyById(surveyId);
        return new SurveyResponseDto(survey);
    }

    @Transactional
    public List<SurveyResponseDto> getAllSurvey() {
        return surveyRepository.findAll().stream().map(SurveyResponseDto::new).toList();
    }

    @Transactional
    public MessageDto updateSurvey(Long surveyId, SurveyRequestDto requestDto, User user) {
        Survey survey = getSurveyById(surveyId);
        if (!survey.getUser().getUserId().equals(user.getUserId())&&user.getRole().equals(UserRoleEnum.USER)) {
            throw new IllegalArgumentException("(임시)권한이 없음");
        }
        survey.update(requestDto);
        return new MessageDto("수정이 완료되었습니다");
    }

    @Transactional
    public MessageDto deleteSurvey(Long surveyId, User user) {
        Survey survey = getSurveyById(surveyId);
        if (!survey.getUser().getUserId().equals(user.getUserId())&&user.getRole().equals(UserRoleEnum.USER)) {
            throw new IllegalArgumentException("(임시)권한이 없음");
        }
        surveyRepository.delete(survey);
        return new MessageDto("삭제가 완료되었습니다");
    }

    @Transactional
    private Survey getSurveyById(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(()-> new IllegalArgumentException("(임시) 설문을 찾을 수 없음"));
        return survey;
    }

}
