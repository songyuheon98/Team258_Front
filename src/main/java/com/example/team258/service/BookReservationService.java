package com.example.team258.service;

import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookReservation;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.entity.User;
import com.example.team258.repository.BookRepository;
import com.example.team258.repository.BookReservationRepository;
import com.example.team258.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookReservationService {
    private final BookReservationRepository bookReservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto createReservation(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new IllegalArgumentException("book을 찾을 수 없습니다."));
        User savedUser = userRepository.findById(user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("user를 찾을 수 없습니다."));
        if (book.getBookStatus() == BookStatusEnum.POSSIBLE) {
            throw new IllegalArgumentException("책이 대여 가능한 상태입니다.");
        }

        BookReservation bookReservation = bookReservationRepository.save(new BookReservation(savedUser, book));
        book.addBookReservation(bookReservation);
        savedUser.addBookReservation(bookReservation);//추후 하나의 메소드로 통합하는 방안도 확인하겠음

        return new MessageDto("도서 예약 신청이 완료되었습니다");
    }

    @Transactional
    public MessageDto deleteReservation(Long bookId, User user) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(()->new IllegalArgumentException("book을 찾을 수 없습니다."));
        User savedUser = userRepository.findById(user.getUserId())
                .orElseThrow(()->new IllegalArgumentException("user를 찾을 수 없습니다."));
        BookReservation bookReservation = bookReservationRepository.findByUserAndBook(savedUser, book)
                .orElseThrow(()->new IllegalArgumentException("대여 기록을 찾을 수 없습니다"));

        bookReservationRepository.deleteById(bookReservation.getBookReservationId());

        return new MessageDto("도서 예약 취소가 완료되었습니다");
    }
}
