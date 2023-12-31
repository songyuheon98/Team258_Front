//package com.example.team258.service;
//
//import com.example.team258.aaaDonation.dto.BookDonationEventRequestDto;
//import com.example.team258.aaaDonation.dto.BookDonationEventResponseDto;
//import com.example.team258.dto.MessageDto;
//import com.example.team258.aaaDonation.entity.BookDonationEvent;
//import com.example.team258.entity.User;
//import com.example.team258.entity.UserRoleEnum;
//import com.example.team258.jwt.SecurityUtil;
//import com.example.team258.aaaDonation.repository.BookDonationEventRepository;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.mockito.MockedStatic;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.*;
//
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//@SpringBootTest
//@AutoConfigureTestDatabase
//@AutoConfigureMockMvc(addFilters = false)
//class BookDonationEventServiceTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private BookDonationEventRepository bookDonationEventRepository;
//
//    @MockBean
//    private SecurityUtil securityUtil;
//
//    @Autowired
//    private BookDonationEventService bookDonationEventService;
//    private static MockedStatic<SecurityUtil> mockedSecurityUtil;
//    @BeforeAll
//    static void setUp() {
//        mockedSecurityUtil = mockStatic(SecurityUtil.class);
//    }
//    @AfterAll
//    static void tearDown() {
//        mockedSecurityUtil.close();
//    }
//
//    @Test
//    void createDonationEvent() {
//        // given
//        MessageDto msg =  MessageDto.builder()
//                .msg("이벤트추가가 완료되었습니다")
//                .build();
//
//        when(bookDonationEventRepository.save(any(BookDonationEvent.class))).thenReturn(new BookDonationEvent());
//
//        // when
//        ResponseEntity<MessageDto> result = bookDonationEventService.createDonationEvent(new BookDonationEventRequestDto());
//
//        // then
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(result.getBody().getMsg()).isEqualTo("이벤트추가가 완료되었습니다");
//
//    }
//
//    @Test
//    void updateDonationEvent() {
//        // given
//
//        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));
//
//        // when
//        ResponseEntity<MessageDto> result = bookDonationEventService.updateDonationEvent(1L,new BookDonationEventRequestDto());
//
//        // then
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(result.getBody().getMsg()).isEqualTo("이벤트 수정이 완료되었습니다");
//    }
//
//    @Test
//    void updateDonationEvent_Event_is_Null() {
//        // given
//        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//        // when
//        // then
//        assertThrows(IllegalArgumentException.class,
//                () -> bookDonationEventService.updateDonationEvent(1L,new BookDonationEventRequestDto()));
//    }
//
//
//    @Test
//    void deleteDonationEvent() {
//        // given
//        User user = User.builder()
//                .role(UserRoleEnum.ADMIN)
//                .build();
//
//        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
//        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));
//        doNothing().when(bookDonationEventRepository).delete(any(BookDonationEvent.class));
//
//        // when
//        ResponseEntity<MessageDto> result = bookDonationEventService.deleteDonationEvent(1L);
//
//        // then
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
//        assertThat(result.getBody().getMsg()).isEqualTo("이벤트 삭제가 완료되었습니다");
//    }
//
//    @Test
//    void deleteDonationEvent_No_Event() {
//        // given
//        User user = User.builder()
//                .role(UserRoleEnum.ADMIN)
//                .build();
//
//        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
//        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//        doNothing().when(bookDonationEventRepository).delete(any(BookDonationEvent.class));
//
//        // when
//        // then
//        assertThrows(IllegalArgumentException.class,()-> bookDonationEventService.deleteDonationEvent(1L));
//    }
//
//    @Test
//    void deleteDonationEvent_No_Admin() {
//        // given
//        User user = User.builder()
//                .role(UserRoleEnum.USER)
//                .build();
//
//        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
//        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));
//        doNothing().when(bookDonationEventRepository).delete(any(BookDonationEvent.class));
//
//        // when
//        ResponseEntity<MessageDto> result = bookDonationEventService.deleteDonationEvent(1L);
//
//        // then
//        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
//        assertThat(result.getBody().getMsg()).isEqualTo("관리자만 이벤트를 삭제할 수 있습니다.");
//    }
//
//
//    @Test
//    void getDonationEvent() {
//        // given
//        List<BookDonationEvent> bookDonationEvents = new ArrayList<>();
//        bookDonationEvents.add(
//                BookDonationEvent.builder()
//                        .donationId(1L)
//                        .createdAt(LocalDateTime.parse("2023-10-12T19:16:01"))
//                        .closedAt(LocalDateTime.parse("2023-10-12T19:16:59"))
//                        .build()
//        );
//
//        when(bookDonationEventRepository.findAll()).thenReturn(bookDonationEvents);
//
//
//        // when
//        List<BookDonationEventResponseDto> result = bookDonationEventService.getDonationEvent();
//
//        // then
//        assertThat(result.get(0).getdonationId()).isEqualTo(1L);
//        assertThat(result.get(0).getCreatedAt()).isEqualTo(LocalDateTime.parse("2023-10-12T19:16:01"));
//        assertThat(result.get(0).getClosedAt()).isEqualTo(LocalDateTime.parse("2023-10-12T19:16:59"));
//    }
//
//    @Test
//    void getDonationEvent_NULL() {
//        // given
//        List<BookDonationEvent> bookDonationEvents = new ArrayList<>();
//        bookDonationEvents.add(
//                BookDonationEvent.builder()
//                        .build()
//        );
//
//        when(bookDonationEventRepository.findAll()).thenReturn(bookDonationEvents);
//
//
//        // when
//        List<BookDonationEventResponseDto> result = bookDonationEventService.getDonationEvent();
//
//        // then
//        assertThat(result.get(0).getdonationId()).isNull();
//    }
//
//}