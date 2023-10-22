package com.example.team258.domain.donation.service;

import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.donation.dto.*;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.donation.repository.BookApplyDonationRepository;
import com.example.team258.domain.donation.repository.BookDonationEventRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.parse;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookDonationEventServiceTest {

    @InjectMocks BookDonationEventService bookDonationEventService;
    @Mock BookDonationEventRepository bookDonationEventRepository;
    @Mock private SecurityUtil securityUtil;
    @Mock
    BookRepository bookRepository;
    @Mock
    BookApplyDonationRepository bookApplyDonationRepository;
    private static MockedStatic<SecurityUtil> mockedSecurityUtil;
    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockedSecurityUtil = mockStatic(SecurityUtil.class);
    }
    @AfterAll
    void tearDown() {
        mockedSecurityUtil.close();
    }
    @Test
    void createDonationEvent() {
        //given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .createdAt(parse("2021-09-01T00:00:00"))
                .closedAt(parse("2021-09-30T00:00:00"))
                .build();
        when(bookDonationEventRepository.save(any(BookDonationEvent.class))).thenReturn(bookDonationEvent);

        //when
        ResponseEntity<MessageDto> result = bookDonationEventService.createDonationEvent(BookDonationEventRequestDto.builder().build());

        //then
        assertThat(result.getBody().getMsg()).isEqualTo("이벤트추가가 완료되었습니다");
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void updateDonationEvent() {
        //given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .createdAt(parse("2021-09-01T00:00:00"))
                .closedAt(parse("2021-09-30T00:00:00"))
                .build();
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(bookDonationEvent));

        //when
        ResponseEntity<MessageDto> result = bookDonationEventService.updateDonationEvent(1L, BookDonationEventRequestDto.builder().build());

        //then
        assertThat(result.getBody().getMsg()).isEqualTo("이벤트 수정이 완료되었습니다");
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void deleteDonationEvent() {
        // given
        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.ADMIN)
                .bookApplyDonations(new ArrayList<>())
                .build();
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .applyId(1L)
                .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                .build();

        bookDonationEvent.addBook(book);

        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(bookApplyDonationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookApplyDonation));
        // when
        ResponseEntity<MessageDto> result = bookDonationEventService.deleteDonationEvent(1L);

        // then
        assertThat(result.getBody().getMsg()).isEqualTo("이벤트 삭제가 완료되었습니다");
        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void deleteDonationEvent_이벤트삭제_주체가_user일때() {
        // given
        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .bookApplyDonations(new ArrayList<>())
                .build();
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .applyId(1L)
                .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                .build();

        bookDonationEvent.addBook(book);

        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(bookApplyDonationRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookApplyDonation));
        // when
        ResponseEntity<MessageDto> result = bookDonationEventService.deleteDonationEvent(1L);

        // then
        assertThat(result.getBody().getMsg()).isEqualTo("관리자만 이벤트를 삭제할 수 있습니다.");
        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }
    @Test
    void getDonationEvent() {
// given
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .applyId(1L)
                .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                .build();
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

        bookDonationEvent.addBook(book);
        bookDonationEvent.getBookApplyDonations().add(bookApplyDonation);

        List<BookDonationEvent> bookDonationEvents = new ArrayList<>();
        bookDonationEvents.add(bookDonationEvent);

        when(bookDonationEventRepository.findAll()).thenReturn(bookDonationEvents);


        // when
        List<BookDonationEventResponseDto> result = bookDonationEventService.getDonationEvent();

        // then
        assertThat(result.get(0).getDonationId()).isEqualTo(1L);
        assertThat(result.get(0).getCreatedAt()).isEqualTo(parse("2021-07-01T00:00:00"));
        assertThat(result.get(0).getClosedAt()).isEqualTo(parse("2024-09-01T00:00:00"));
        assertThat(result.get(0).getBookResponseDtos().get(0).getBookId()).isEqualTo(1L);
        assertThat(result.get(0).getBookResponseDtos().get(0).getBookName()).isEqualTo("bookName");
        assertThat(result.get(0).getBookResponseDtos().get(0).getBookAuthor()).isEqualTo("bookAuthor");
        assertThat(result.get(0).getBookResponseDtos().get(0).getBookPublish()).isEqualTo("2011");
        assertThat(result.get(0).getBookResponseDtos().get(0).getBookStatus()).isEqualTo(BookStatusEnum.POSSIBLE);
    }

    @Test
    void getDonationEventV2() {
        // given
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("2011")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .applyId(1L)
                .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                .build();
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

        bookDonationEvent.addBook(book);
        bookDonationEvent.getBookApplyDonations().add(bookApplyDonation);

        Page<BookDonationEvent> bookDonationEvents = new PageImpl<>(List.of(bookDonationEvent));

        when(bookDonationEventRepository.findAll(any(Pageable.class))).thenReturn(bookDonationEvents);

        // when
        BookDonationEventPageResponseDto result = bookDonationEventService.getDonationEventV2(Pageable.unpaged());

        // then
        assertThat(result.getBookDonationEventResponseDtos().get(0).getDonationId()).isEqualTo(1L);
        assertThat(result.getBookDonationEventResponseDtos().get(0).getCreatedAt()).isEqualTo(parse("2021-07-01T00:00:00"));
        assertThat(result.getBookDonationEventResponseDtos().get(0).getClosedAt()).isEqualTo(parse("2024-09-01T00:00:00"));
    }

    @Test
    void getDonationEventV3() {
        // given
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        BookApplyDonation bookApplyDonation = BookApplyDonation.builder()
                .applyId(1L)
                .applyDate(LocalDateTime.parse("2021-08-01T00:00:00"))
                .build();
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

        bookDonationEvent.addBook(book);
        bookDonationEvent.getBookApplyDonations().add(bookApplyDonation);

        Page<BookDonationEvent> bookDonationEvents = new PageImpl<>(List.of(bookDonationEvent));

        when(bookDonationEventRepository.findAll(any(Pageable.class))).thenReturn(bookDonationEvents);

        // when
        BookDonationEventPageResponseDtoV3 result = bookDonationEventService.getDonationEventV3(Pageable.unpaged());

        // then
       assertThat(result.getBookDonationEventResponseDtoV3().get(0).getDonationId()).isEqualTo(1L);
       assertThat(result.getBookDonationEventResponseDtoV3().get(0).getCreatedAt()).isEqualTo(parse("2021-07-01T00:00:00"));
       assertThat(result.getBookDonationEventResponseDtoV3().get(0).getClosedAt()).isEqualTo(parse("2024-09-01T00:00:00"));
       assertThat(result.getTotalPages()).isEqualTo(1);
    }

    @Test
    void getDonationEventOnlyV2() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

        Page<BookDonationEvent> bookDonationEvents = new PageImpl<>(List.of(bookDonationEvent));

        when(bookDonationEventRepository.findAll(any(Pageable.class))).thenReturn(bookDonationEvents);

        // when
        BookDonationEventOnlyPageResponseDto result = bookDonationEventService.getDonationEventOnlyV2(PageRequest.of(0, 1));

        // then
        assertThat(result.getBookDonationEventOnlyResponseDtos().get(0).getDonationId()).isEqualTo(1L);
        assertThat(result.getBookDonationEventOnlyResponseDtos().get(0).getCreatedAt()).isEqualTo(parse("2021-07-01T00:00:00"));
        assertThat(result.getBookDonationEventOnlyResponseDtos().get(0).getClosedAt()).isEqualTo(parse("2024-09-01T00:00:00"));
        assertThat(result.getTotalpages()).isEqualTo(1);
    }

    @Test
    void getDonationEventOnlyV3() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();
        Page<BookDonationEvent> bookDonationEvents = new PageImpl<>(List.of(bookDonationEvent));

        when(bookDonationEventRepository.findAll(any(BooleanBuilder.class),any(Pageable.class))).thenReturn(bookDonationEvents);

        // when
        BookDonationEventOnlyPageResponseDto result = bookDonationEventService.getDonationEventOnlyV3(PageRequest.of(0, 1), 1L, null, null);

        // then
        assertThat(result.getBookDonationEventOnlyResponseDtos().get(0).getDonationId()).isEqualTo(1L);
        assertThat(result.getBookDonationEventOnlyResponseDtos().get(0).getCreatedAt()).isEqualTo(parse("2021-07-01T00:00:00"));
        assertThat(result.getBookDonationEventOnlyResponseDtos().get(0).getClosedAt()).isEqualTo(parse("2024-09-01T00:00:00"));
        assertThat(result.getTotalpages()).isEqualTo(1);
    }

    @Test
    void getDonationEventPage() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .build();
        List<BookDonationEvent> bookDonationEvents = new ArrayList<>();
        bookDonationEvents.add(bookDonationEvent);
        when(bookDonationEventRepository.findAll()).thenReturn(bookDonationEvents);

        // when
        List<BookDonationEventResponseDto> result = bookDonationEventService.getDonationEventPage();

        // then
        assertThat(result.get(0).getDonationId()).isEqualTo(1L);
        assertThat(result.get(0).getCreatedAt()).isEqualTo(parse("2021-07-01T00:00:00"));
        assertThat(result.get(0).getClosedAt()).isEqualTo(parse("2024-09-01T00:00:00"));
    }

    @Test
    void settingDonationEvent() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        BookDonationSettingRequestDto bookDonationSettingRequestDto = BookDonationSettingRequestDto.builder()
                .donationId(1L)
                .bookIds(List.of(1L))
                .build();

        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));

        // when
        MessageDto result = bookDonationEventService.settingDonationEvent(bookDonationSettingRequestDto);

        // then
        assertThat(result.getMsg()).isEqualTo("이벤트 설정이 완료되었습니다");

    }

    @Test
    void settingCancelDonationEvent() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();

        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));

        bookDonationEvent.addBook(book);

        // when
        MessageDto result = bookDonationEventService.settingCancelDonationEvent(BookDonationSettingCancelRequestDto.builder()
                .donationId(1L)
                .bookId(1L)
                .build());

        // then
        assertThat(result.getMsg()).isEqualTo("해당 도서가 이벤트에서 삭제 완료되었습니다");

    }

    @Test
    void settingCancelDonationEvent_해당_이벤트가_존재하지_않을_때() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();

        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));

        bookDonationEvent.addBook(book);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            bookDonationEventService.settingCancelDonationEvent(BookDonationSettingCancelRequestDto.builder()
                    .bookId(1L)
                    .build());
        });
    }
    @Test
    void settingCancelDonationEvent_해당_책이_존재하지_않을_때() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();

        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(bookRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(book));

        bookDonationEvent.addBook(book);

        // when
        // then
        assertThrows(IllegalArgumentException.class, () -> {
            bookDonationEventService.settingCancelDonationEvent(BookDonationSettingCancelRequestDto.builder()
                            .donationId(1L)
                    .build());
        });
    }
    @Test
    void endDonationEvent() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.ADMIN)
                .bookApplyDonations(new ArrayList<>())
                .build();

        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        doNothing().when(bookDonationEventRepository).delete(bookDonationEvent);

        // when
        MessageDto result = bookDonationEventService.endDonationEvent(1L);

        // then
        assertThat(result.getMsg()).isEqualTo("이벤트 종료가 완료되었습니다");
    }

    @Test
    void endDonationEvent_관리자가_아닐_때() {
        // given
        BookDonationEvent bookDonationEvent = BookDonationEvent.builder()
                .donationId(1L)
                .createdAt(LocalDateTime.parse("2021-07-01T00:00:00"))
                .closedAt(LocalDateTime.parse("2024-09-01T00:00:00"))
                .bookApplyDonations(new ArrayList<>())
                .books(new ArrayList<>())
                .build();

        User user = User.builder()
                .userId(1L)
                .username("username")
                .password("password")
                .role(UserRoleEnum.USER)
                .bookApplyDonations(new ArrayList<>())
                .build();

        when(bookDonationEventRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(bookDonationEvent));
        when(SecurityUtil.getPrincipal()).thenReturn(Optional.ofNullable(user));
        doNothing().when(bookDonationEventRepository).delete(bookDonationEvent);

        // when
        MessageDto result = bookDonationEventService.endDonationEvent(1L);

        // then
        assertThat(result.getMsg()).isEqualTo("관리자만 이벤트를 종료할 수 있습니다.");
    }

}