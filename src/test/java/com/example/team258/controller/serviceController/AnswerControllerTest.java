package com.example.team258.controller.serviceController;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.*;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AnswerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class AnswerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnswerService answerService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void AnswerCreateTest() throws Exception{
        AnswerRequestDto requestDto = AnswerRequestDto.builder()
                .answer(1L)
                .surveyId(1L)
                .build();

        User user = User.builder()
                .userId(1L)
                .role(UserRoleEnum.USER)
                .password("password")
                .username("username")
                .build();


        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        MessageDto msg =  MessageDto.builder()
                .msg("작성이 완료되었습니다.")
                .build();

        when(answerService.createAnswer(any(AnswerRequestDto.class),any(User.class))).thenReturn(AnswerResponseDto.builder()
                        .answer(1L).surveyId(1L).answer(1L).build()
        );

        mockMvc.perform(post("/api/answer")
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value(1L))
                .andExpect(jsonPath("$.surveyId").value(1L))
                .andExpect(jsonPath("$.answer").value(1L))
                .andDo(print());

    }

    @Test
    void AnswerGetTest() throws Exception{
        User user = User.builder()
                .userId(1L)
                .role(UserRoleEnum.USER)
                .password("password")
                .username("username")
                .build();

        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("질문질문")
                .choices("대충 답지 3개 있음")
                .deadline(LocalDateTime.of(2023,10,31,0,0))
                .maxChoice(3L)
                .user(user)
                .build();

        Answer answer = Answer.builder()
                .answerId(1L)
                .answerNum(1L)
                .survey(survey)
                .user(user)
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        List<AnswerResponseDto> answerResponseDtoList = new ArrayList<>();
        answerResponseDtoList.add(new AnswerResponseDto(answer));
        when(answerService.getAnswers(any(User.class))).thenReturn(answerResponseDtoList);

        mockMvc.perform(get("/api/answer")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk());
    }

    @Test
    void AnswerUpdateTest() throws Exception{
        User user = User.builder()
                .userId(1L)
                .role(UserRoleEnum.USER)
                .password("password")
                .username("username")
                .build();

        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("질문질문")
                .choices("대충 답지 3개 있음")
                .deadline(LocalDateTime.of(2023,10,31,0,0))
                .maxChoice(3L)
                .user(user)
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        MessageDto msg =  MessageDto.builder()
                .msg("수정이 완료되었습니다.")
                .build();

        AnswerRequestDto requestDto = AnswerRequestDto.builder()
                .answer(1L)
                .surveyId(1L)
                .build();

        when(answerService.updateAnswer(any(AnswerRequestDto.class),any(Long.class), any(User.class))).thenReturn(msg);
        mockMvc.perform(put("/api/answer/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("수정이 완료되었습니다."));
    }

    @Test
    void AnswerDeleteTest() throws Exception{
        User user = User.builder()
                .userId(1L)
                .role(UserRoleEnum.USER)
                .password("password")
                .username("username")
                .build();

        Survey survey = Survey.builder()
                .surveyId(1L)
                .question("질문질문")
                .choices("대충 답지 3개 있음")
                .deadline(LocalDateTime.of(2023,10,31,0,0))
                .maxChoice(3L)
                .user(user)
                .build();

        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        MessageDto msg =  MessageDto.builder()
                .msg("삭제가 완료되었습니다.")
                .build();

        when(answerService.deleteAnswer(any(Long.class), any(User.class))).thenReturn(msg);
        mockMvc.perform(delete("/api/answer/1")
                        .with(SecurityMockMvcRequestPostProcessors.user(userDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("삭제가 완료되었습니다."));
    }
}
