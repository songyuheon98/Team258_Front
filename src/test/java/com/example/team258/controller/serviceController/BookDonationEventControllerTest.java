package com.example.team258.controller.serviceController;

import com.example.team258.dto.BookDonationEventRequestDto;
import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.BookDonationEvent;
import com.example.team258.service.BookDonationEventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
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

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class BookDonationEventControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookDonationEventService bookDonationEventService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void createDonationEvent() throws Exception {
        // given
        MessageDto msg =  MessageDto.builder()
                .msg("이벤트추가가 완료되었습니다")
                .build();

        when(bookDonationEventService.createDonationEvent(any(BookDonationEventRequestDto.class)))
                .thenReturn(new ResponseEntity<>(msg, HttpStatus.OK));

        // when
        // then
        mockMvc.perform(post("/api/admin/donation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookDonationEventRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("이벤트추가가 완료되었습니다"));

    }


    @Test
    void updateDonationEvent() throws Exception {
        // given
        MessageDto msg =  MessageDto.builder()
                .msg("이벤트 수정이 완료되었습니다")
                .build();

        when(bookDonationEventService.updateDonationEvent(any(Long.class),any(BookDonationEventRequestDto.class)))
                .thenReturn(new ResponseEntity<>(msg, HttpStatus.OK));

        // when
        // then
        mockMvc.perform(put("/api/admin/donation/{donationId}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BookDonationEventRequestDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("이벤트 수정이 완료되었습니다"));

    }

    @Test
    void deleteDonationEvent() throws Exception {
        // given
        MessageDto msg =  MessageDto.builder()
                .msg("이벤트 삭제가 완료되었습니다")
                .build();

        when(bookDonationEventService.deleteDonationEvent(any(Long.class)))
                .thenReturn(new ResponseEntity<>(msg, HttpStatus.OK));

        // when
        // then
        mockMvc.perform(delete("/api/admin/donation/{donationId}",1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("이벤트 삭제가 완료되었습니다"));

    }

    @Test
    void selectDonationEvent() throws Exception {
        // given
        List<BookDonationEventResponseDto> bookDonationEventResponseDtos = new ArrayList<>();
        bookDonationEventResponseDtos.add(
                new BookDonationEventResponseDto(
                        BookDonationEvent.builder()
                        .donationId(1L)
                        .createdAt(LocalDateTime.parse("2023-10-12T19:16:01"))
                        .closedAt(LocalDateTime.parse("2023-10-12T19:16:59"))
                                .books(new ArrayList<>())
                        .build()
                )
        );

        when(bookDonationEventService.getDonationEvent())
                .thenReturn(bookDonationEventResponseDtos);

        // when
        // then
        mockMvc.perform(get("/api/admin/donation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].donationId").value(1L))
                .andExpect(jsonPath("$[0].createdAt").value("2023-10-12T19:16:01"))
                .andExpect(jsonPath("$[0].closedAt").value("2023-10-12T19:16:59"))
                .andDo(print());

    }
}