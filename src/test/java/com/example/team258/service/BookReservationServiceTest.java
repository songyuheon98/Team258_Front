package com.example.team258.service;

import com.example.team258.dto.MessageDto;
import com.example.team258.entity.*;
import com.example.team258.repository.BookRentRepository;
import com.example.team258.repository.BookRepository;
import com.example.team258.repository.BookReservationRepository;
import com.example.team258.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class BookReservationServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookReservationService bookReservationService;

    @MockBean
    private BookReservationRepository bookReservationRepository;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private UserRepository userRepository;

    User user1 = User.builder()
            .userId(1L)
            .username("유저1")
            .password("123")
            .role(UserRoleEnum.USER)
            .bookRents(new ArrayList<BookRent>())
            .bookReservations(new ArrayList<BookReservation>())
            .build();
    Book book1 = Book.builder()
            .bookId(1L)
            .bookName("책1")
            .bookAuthor("작가1")
            .bookPublish("2011")
            .bookReservations(new ArrayList<BookReservation>())
            .bookStatus(BookStatusEnum.POSSIBLE)
            .build();
    Book book2 = Book.builder()
            .bookId(2L)
            .bookName("책2")
            .bookAuthor("작가2")
            .bookPublish("2011")
            .bookReservations(new ArrayList<BookReservation>())
            .bookStatus(BookStatusEnum.IMPOSSIBLE)
            .build();

    @Test
    void createReservation_성공() {
        //given
        book1.changeStatus(BookStatusEnum.IMPOSSIBLE);
        BookReservation bookReservation1 = BookReservation.builder()
                .bookReservationId(1L)
                .book(book1)
                .user(user1)
                .build();
        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(bookReservationRepository.save(any(BookReservation.class))).thenReturn(bookReservation1);
        //then
        MessageDto result = bookReservationService.createReservation(1L, user1);
        assertThat(result.getMsg()).isEqualTo("도서 예약 신청이 완료되었습니다");
        assertThat(book1.getBookReservations()).contains(bookReservation1);
        assertThat(user1.getBookReservations()).contains(bookReservation1);
    }

    @Test
    void createReservation_실패_book없음() {
        //given

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookReservationService.createReservation(2L, user1));
        assertThat(e.getMessage()).isEqualTo("book을 찾을 수 없습니다.");
    }

    @Test
    void createReservation_실패_book대여가능() {
        //given

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookReservationService.createReservation(1L, user1));
        assertThat(e.getMessage()).isEqualTo("책이 대여 가능한 상태입니다.");
    }

    @Test
    void deleteReservation_성공() {
        //given
        book1.changeStatus(BookStatusEnum.IMPOSSIBLE);
        BookReservation bookReservation1 = BookReservation.builder()
                .bookReservationId(1L)
                .book(book1)
                .user(user1)
                .build();
        book1.addBookReservation(bookReservation1);
        user1.addBookReservation(bookReservation1);

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));
        when(bookReservationRepository.findByUserAndBook(user1, book1)).thenReturn(Optional.of(bookReservation1));
        //then
        MessageDto result = bookReservationService.deleteReservation(1L, user1);
        assertThat(result.getMsg()).isEqualTo("도서 예약 취소가 완료되었습니다");
    }

    @Test
    void deleteReservation_실패_book없음() {
        //given

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookReservationService.deleteReservation(2L, user1));
        assertThat(e.getMessage()).isEqualTo("book을 찾을 수 없습니다.");
    }

    @Test
    void deleteReservation_실패_대여기록없음() {
        //given

        //when
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book1));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        //then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, () ->
                bookReservationService.deleteReservation(1L, user1));
        assertThat(e.getMessage()).isEqualTo("예약 기록을 찾을 수 없습니다");
    }
}