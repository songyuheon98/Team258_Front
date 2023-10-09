package com.example.team258.service;

import com.example.team258.dto.SurveyRequestDto;
import com.example.team258.dto.SurveyResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.SurveyRepository;
import com.example.team258.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class SurveyServiceTest {
    @Autowired
    private SurveyService surveyService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private SurveyRepository surveyRepository;


    User user1 = User.builder()
            .userId(1L)
            .username("유저1")
            .password("123")
            .surveys(new ArrayList<>())
            .role(UserRoleEnum.USER)
            .build();
    User user2 = User.builder()
            .userId(2L)
            .username("유저2")
            .password("123")
            .surveys(new ArrayList<>())
            .role(UserRoleEnum.USER)
            .build();
    User admin = User.builder()
            .userId(3L)
            .username("어드민")
            .password("123")
            .surveys(new ArrayList<>())
            .role(UserRoleEnum.ADMIN)
            .build();
    SurveyRequestDto requestDto2 = SurveyRequestDto.builder()
            .question("테스트질문수정함")
            .choices("1. a, 2. b")
            .maxChoice(2L)
            .deadline(LocalDateTime.of(2025, 10, 23, 15, 00))
            .build();

    @Test
    void Survey_생성_성공() {
        //given
        SurveyRequestDto requestDto = SurveyRequestDto.builder()
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .build();

        // when
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user1));

        // then
        MessageDto result = surveyService.createSurvey(requestDto, user1);
        assertThat(result.getMsg()).isEqualTo("작성이 완료되었습니다");
    }

    @Test
    void Survey_1개_조회_성공() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));
        
        //then
        SurveyResponseDto result = surveyService.getSurvey(1L);
        assertThat(result.getQuestion()).isEqualTo("테스트질문");
        assertThat(result.getChoices()).isEqualTo("1. a, 2. b, 3. c");
    }

    @Test
    void Survey_1개_조회_실패() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));

        //then
        assertThrows(IllegalArgumentException.class, () ->
                surveyService.getSurvey(2L));
    }

    @Test
    void Survey_전체_조회_성공() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();
        List<Survey> list = new ArrayList<>();
        list.add(survey);
        list.add(survey);

        //when
        when(surveyRepository.findAll()).thenReturn(list);

        //then
        List<SurveyResponseDto> result = surveyService.getAllSurvey();
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void Survey_수정_성공() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));

        //then
        MessageDto result = surveyService.updateSurvey(1L, requestDto2, user1);
        assertThat(result.getMsg()).isEqualTo("수정이 완료되었습니다");
    }
    @Test
    void Survey_수정_성공_admin() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));

        //then
        MessageDto result = surveyService.updateSurvey(1L, requestDto2, admin);
        assertThat(result.getMsg()).isEqualTo("수정이 완료되었습니다");
    }
    @Test
    void Survey_수정_실패() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));

        //then
        assertThrows(IllegalArgumentException.class, () ->
                surveyService.updateSurvey(1L, requestDto2, user2));
    }

    @Test
    void Survey_삭제_성공() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));

        //then
        MessageDto result = surveyService.deleteSurvey(1L, user1);
        assertThat(result.getMsg()).isEqualTo("삭제가 완료되었습니다");
    }
    @Test
    void Survey_삭제_성공_admin() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));

        //then
        MessageDto result = surveyService.deleteSurvey(1L, admin);
        assertThat(result.getMsg()).isEqualTo("삭제가 완료되었습니다");
    }
    @Test
    void Survey_삭제_실패() {
        //given
        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("테스트질문")
                .choices("1. a, 2. b, 3. c")
                .maxChoice(3L)
                .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
                .user(user1)
                .build();

        //when
        when(surveyRepository.findById(1L)).thenReturn(Optional.ofNullable(survey));

        //then
        assertThrows(IllegalArgumentException.class, () ->
                surveyService.deleteSurvey(1L, user2));
    }
}






























