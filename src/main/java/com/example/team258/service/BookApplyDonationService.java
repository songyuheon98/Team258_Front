package com.example.team258.service;

import com.example.team258.dto.BookApplyDonationRequestDto;
import com.example.team258.dto.BookApplyDonationResponseDto;
import com.example.team258.dto.BookResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.*;
import com.example.team258.jwt.SecurityUtil;
import com.example.team258.repository.BookDonationEventRepository;
import com.example.team258.repository.BookRepository;
import com.example.team258.repository.BookApplyDonationRepository;
import com.example.team258.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookApplyDonationService {

    private final BookRepository bookRepository;
    private final BookDonationEventRepository bookDonationEventRepository;
    private final BookApplyDonationRepository bookApplyDonationRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<MessageDto> createBookApplyDonation(BookApplyDonationRequestDto bookApplyDonationRequestDto) {
        /**
         * 나눔 책이 존재하지 않을때
         */
        Book book = bookRepository.findById(bookApplyDonationRequestDto.getBookId())
                .orElseThrow(()->new IllegalArgumentException("나눔 신청한 책이 존재하지 않습니다."));
        /**
         * 나눔 신청한 책이 나눔 상태가 아닐때
         */

        /**
         * 누군가 먼저 신청했을때
         */
        if(book.getBookApplyDonation()!=null){
            return ResponseEntity.ok().body(new MessageDto("이미 누군가 먼저 신청했습니다."));
        }

        /**
         * 나눔 이벤트 시간이 아닐때
         */
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(bookApplyDonationRequestDto.getDonationId())
                .orElseThrow(()->new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));

        if(bookApplyDonationRequestDto.getApplyDate().isBefore(bookDonationEvent.getCreatedAt()) ||
                bookApplyDonationRequestDto.getApplyDate().isAfter( bookDonationEvent.getClosedAt())){
            return ResponseEntity.ok().body(new MessageDto("책 나눔 이벤트 기간이 아닙니다."));
        }

        /**
         * 신청자가 도서관 사용자가 아닐때
         */
        User user = userRepository.findById(SecurityUtil.getPrincipal().get().getUserId()).orElseThrow(
                ()->new IllegalArgumentException("해당 사용자는 도서관 사용자가 아닙니다.")
        );


        /**
         * 나눔 신청
         */
        BookApplyDonation bookApplyDonation = new BookApplyDonation(bookApplyDonationRequestDto);
        bookApplyDonationRepository.save(bookApplyDonation);

        /**
         * book과 편의 메소드로 양방향 관계 설정
         */
        bookApplyDonation.addBook(book);

        /**
         * user, bookDonationEvent와 단방향 관계 설정
         */
        user.getBookApplyDonations().add(bookApplyDonation);
        bookDonationEvent.getBookApplyDonations().add(bookApplyDonation);

        return ResponseEntity.ok().body(new MessageDto("책 나눔 신청이 완료되었습니다."));
    }



    @Transactional
    public ResponseEntity<MessageDto> deleteBookApplyDonation(Long applyId) {
        BookApplyDonation bookApplyDonation = bookApplyDonationRepository.findById(applyId)
                .orElseThrow(()->new IllegalArgumentException("해당 신청이 존재하지 않습니다."));

        /**
         * 연관관계 해제 편의 메소드 사용
         */
        bookApplyDonation.removeBook(bookApplyDonation.getBook());

        /**
         * 책나눔 신청 취소
         */
        bookApplyDonationRepository.delete(bookApplyDonation);

        return ResponseEntity.ok().body(new MessageDto("책 나눔 신청이 취소되었습니다."));
    }

    public List<BookResponseDto> getDonationBooks(BookStatusEnum bookStatus) {

        List<BookResponseDto> bookResponseDtos= bookRepository.findByBookStatus(bookStatus).stream()
                .map(BookResponseDto::new)
                .toList();
        return bookResponseDtos;
    }

    public List<BookApplyDonationResponseDto> getBookApplyDonations() {
        return bookApplyDonationRepository.findAll().stream()
                .map(BookApplyDonationResponseDto::new)
                .toList();
    }
}
