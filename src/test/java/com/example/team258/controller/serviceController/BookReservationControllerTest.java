package com.example.team258.controller.serviceController;

import com.example.team258.dto.MessageDto;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.BookRepository;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.BookReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class BookReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookReservationService bookReservationService;

    @MockBean
    private BookRepository bookRepository;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    User user1 = User.builder()
            .userId(1L)
            .username("user1")
            .password("1234")
            .role(UserRoleEnum.USER)
            .build();

    UserDetails authenticateUser(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userDetails;
    }

    @Test
    void 대출_생성() throws Exception {
        //given
        MessageDto msg =  MessageDto.builder()
                .msg("도서 예약 신청이 완료되었습니다")
                .build();

        //when
        when(bookReservationService.createReservation(any(), any(User.class)))
                .thenReturn(msg);

        //then
        mockMvc.perform(post("/api/books/{bookId}/reservation", 1L)
                        .content(objectMapper.writeValueAsString(authenticateUser(user1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("도서 예약 신청이 완료되었습니다"));
    }

    @Test
    void 대출_삭제() throws Exception {
        //given
        MessageDto msg =  MessageDto.builder()
                .msg("도서 예약 취소가 완료되었습니다")
                .build();

        //when
        when(bookReservationService.deleteReservation(any(), any(User.class)))
                .thenReturn(msg);

        //then
        mockMvc.perform(delete("/api/books/{bookId}/reservation", 1L)
                        .content(objectMapper.writeValueAsString(authenticateUser(user1))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("도서 예약 취소가 완료되었습니다"));
    }

}