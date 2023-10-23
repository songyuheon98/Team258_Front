package com.example.team258.service;

import com.example.team258.domain.donation.dto.BookDonationEventRequestDto;
import com.example.team258.domain.donation.dto.BookDonationEventResponseDto;
import com.example.team258.domain.donation.service.BookDonationEventService;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.domain.donation.repository.BookApplyDonationRepository;
import com.example.team258.domain.donation.repository.BookDonationEventRepository;
import com.example.team258.common.repository.BookRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDonationEventServiceUnitTest {
    @Mock private BookDonationEventRepository bookDonationEventRepository;
    @Mock private BookRepository bookRepository;

    @Mock private BookApplyDonationRepository bookApplyDonationRepository;
    @Mock private SecurityUtil securityUtil;
    private BookDonationEventService bookDonationEventService;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;

    @BeforeAll
    void setUp() {
        /**
         * Mockito 어노테이션을 활성화하여 Mock 객체 초기화
         */
        MockitoAnnotations.openMocks(this);

        /**
         * Mock 객체를 사용하여 UserService 객체 생성
         */
        mockedSecurityUtil = mockStatic(SecurityUtil.class);

        /**
         * Mock 객체를 사용하여 UserService 객체 생성
         */
        bookDonationEventService = new BookDonationEventService(bookDonationEventRepository,bookRepository,bookApplyDonationRepository);

    }
    @AfterAll
    void tearDown() {
        mockedSecurityUtil.close();
    }

    @Test
    void createDonationEvent() {
        // given
        MessageDto msg =  MessageDto.builder()
                .msg("이벤트추가가 완료되었습니다")
                .build();

        when(bookDonationEventRepository.save(any(BookDonationEvent.class))).thenReturn(new BookDonationEvent());

        // when
        MessageDto result = bookDonationEventService.createDonationEvent(new BookDonationEventRequestDto());

        // then
        assertThat(result.getMsg()).isEqualTo("이벤트추가가 완료되었습니다");

    }

    @Test
    void updateDonationEvent() {
        // given

        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));

        // when
        MessageDto result = bookDonationEventService.updateDonationEvent(1L,new BookDonationEventRequestDto());

        // then
        assertThat(result.getMsg()).isEqualTo("이벤트 수정이 완료되었습니다");
    }

    @Test
    void updateDonationEvent_Event_is_Null() {
        // given
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        // when
        // then
        assertThrows(IllegalArgumentException.class,
                () -> bookDonationEventService.updateDonationEvent(1L,new BookDonationEventRequestDto()));
    }


    @Test
    void deleteDonationEvent() {
        // given
        User user = User.builder()
                .role(UserRoleEnum.ADMIN)
                .build();

        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));
        doNothing().when(bookDonationEventRepository).delete(any(BookDonationEvent.class));

        // when
        MessageDto result = bookDonationEventService.deleteDonationEvent(1L);

        // then
        assertThat(result.getMsg()).isEqualTo("이벤트 삭제가 완료되었습니다");
    }

    @Test
    void deleteDonationEvent_No_Event() {
        // given
        User user = User.builder()
                .role(UserRoleEnum.ADMIN)
                .build();

        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        doNothing().when(bookDonationEventRepository).delete(any(BookDonationEvent.class));

        // when
        // then
        assertThrows(IllegalArgumentException.class,()-> bookDonationEventService.deleteDonationEvent(1L));
    }

    @Test
    void deleteDonationEvent_No_Admin() {
        // given
        User user = User.builder()
                .role(UserRoleEnum.USER)
                .build();

        given(securityUtil.getPrincipal()).willReturn(Optional.ofNullable(user));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.of(new BookDonationEvent()));
        doNothing().when(bookDonationEventRepository).delete(any(BookDonationEvent.class));

        // when
        MessageDto result = bookDonationEventService.deleteDonationEvent(1L);

        // then
        assertThat(result.getMsg()).isEqualTo("관리자만 이벤트를 삭제할 수 있습니다.");
    }


    @Test
    void getDonationEvent() {
        // given
        List<BookDonationEvent> bookDonationEvents = new ArrayList<>();
        bookDonationEvents.add(
                BookDonationEvent.builder()
                        .donationId(1L)
                        .createdAt(LocalDateTime.parse("2023-10-12T19:16:01"))
                        .closedAt(LocalDateTime.parse("2023-10-12T19:16:59"))
                        .build()
        );

        when(bookDonationEventRepository.findAll()).thenReturn(bookDonationEvents);


        // when
        List<BookDonationEventResponseDto> result = bookDonationEventService.getDonationEvent();

        // then
        assertThat(result.get(0).getDonationId()).isEqualTo(1L);
        assertThat(result.get(0).getCreatedAt()).isEqualTo(LocalDateTime.parse("2023-10-12T19:16:01"));
        assertThat(result.get(0).getClosedAt()).isEqualTo(LocalDateTime.parse("2023-10-12T19:16:59"));
    }

    @Test
    void getDonationEvent_NULL() {
        // given
        List<BookDonationEvent> bookDonationEvents = new ArrayList<>();
        bookDonationEvents.add(
                BookDonationEvent.builder()
                        .build()
        );

        when(bookDonationEventRepository.findAll()).thenReturn(bookDonationEvents);


        // when
        List<BookDonationEventResponseDto> result = bookDonationEventService.getDonationEvent();

        // then
        assertThat(result.get(0).getDonationId()).isNull();
    }

}