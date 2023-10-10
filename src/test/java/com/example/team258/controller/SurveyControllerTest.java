package com.example.team258.controller;

import com.example.team258.dto.SurveyRequestDto;
import com.example.team258.dto.SurveyResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Survey;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.SurveyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SurveyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SurveyService surveyService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    User user = User.builder()
            .userId(1L)
            .username("정강용")
            .password("123")
            .role(UserRoleEnum.USER)
            .build();

    SurveyRequestDto requestDto = SurveyRequestDto.builder()
            .question("테스트질문")
            .choices("1. a, 2. b, 3. c")
            .maxChoice(3L)
            .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
            .build();

    Survey survey = Survey.builder()
            .surveyId(1L)
            .question("테스트질문")
            .choices("1. a, 2. b, 3. c")
            .maxChoice(3L)
            .deadline(LocalDateTime.of(2023, 10, 23, 15, 00))
            .user(user)
            .build();

    UserDetails authenticateUser(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userDetails;
    }

    @Test
    void 설문지_생성() throws Exception {

        //given
        SurveyResponseDto surveyResponseDto = SurveyResponseDto.builder()
                .surveyId(1L)
                .question("question")
                .choices("choice")
                .build();

        //when
        when(surveyService.createSurvey(any(SurveyRequestDto.class), any(User.class)))
                .thenReturn(surveyResponseDto);

        //then
        mockMvc.perform(post("/api/surveys")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(surveyResponseDto))
                        .content(objectMapper.writeValueAsString(authenticateUser(user)))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.surveyId").value(1L))
                .andExpect(jsonPath("$.question").value("question"))
                .andExpect(jsonPath("$.choices").value("choice"))
                .andDo(print());
    }

    @Test
    void 설문지_1개_조회() throws Exception {

        //given
        //when
        when(surveyService.getSurvey(any()))
                .thenReturn(new SurveyResponseDto(survey));

        //then
        mockMvc.perform(get("/api/surveys/{surveyId}", 1L))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 설문지_전체_조회() throws Exception {

        //given
        List<SurveyResponseDto> list = new ArrayList<>();
        list.add(new SurveyResponseDto(survey));

        //when
        when(surveyService.getAllSurvey())
                .thenReturn(list);

        //then
        mockMvc.perform(get("/api/surveys"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 설문지_수정() throws Exception {

        //given
        //when
        when(surveyService.updateSurvey(any(Long.class), any(SurveyRequestDto.class), any(User.class)))
                .thenReturn(new MessageDto("수정이 완료되었습니다"));

        //then
        mockMvc.perform(put("/api/surveys/{surveyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                        .content(objectMapper.writeValueAsString(authenticateUser(user))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("수정이 완료되었습니다"));
    }

    @Test
    void 설문지_삭제() throws Exception {

        //given
        //when
        when(surveyService.deleteSurvey(any(Long.class), any(User.class)))
                .thenReturn(new MessageDto("삭제가 완료되었습니다"));

        //then
        mockMvc.perform(delete("/api/surveys/{surveyId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authenticateUser(user))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("삭제가 완료되었습니다"));
    }
}




































