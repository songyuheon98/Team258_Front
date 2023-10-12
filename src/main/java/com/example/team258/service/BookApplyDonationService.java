package com.example.team258.service;

import com.example.team258.dto.BookApplyDonationRequestDto;
import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.BookResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookApplyDonation;
import com.example.team258.entity.BookDonationEvent;
import com.example.team258.repository.BookDonationEventRepository;
import com.example.team258.repository.BookRepository;
import com.example.team258.repository.BookApplyDonationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookApplyDonationService {

    private final BookRepository bookRepository;
    private final BookDonationEventRepository bookDonationEventRepository;
    private final BookApplyDonationRepository bookApplyDonationRepository;
    public ResponseEntity<MessageDto> createBookApplyDonation(Long donationId, BookApplyDonationRequestDto bookApplyDonationRequestDto) {
        /**
         * 나눔 책이 존재하지 않을때
         */
        Book book = bookRepository.findById(bookApplyDonationRequestDto.getBookId())
                .orElseThrow(()->new IllegalArgumentException("나눔 신청한 책이 존재하지 않습니다."));

        /**
         * 누군가 먼저 신청했을때
         */
        if(!book.getBookApplyDonation().equals(null)){
            return ResponseEntity.ok().body(new MessageDto("이미 누군가 먼저 신청했습니다."));
        }

        /**
         * 나눔 이벤트 시간이 아닐때
         */
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(donationId)
                .orElseThrow(()->new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));

        if(bookApplyDonationRequestDto.getApplyDate().isBefore(bookDonationEvent.getCreatedAt()) ||
                bookApplyDonationRequestDto.getApplyDate().isAfter( bookDonationEvent.getClosedAt())){
            throw new IllegalArgumentException("해당 이벤트의 신청 기간이 아닙니다.");
        }

        /**
         * 나눔 신청
         */
        BookApplyDonation bookApplyDonation = new BookApplyDonation(bookApplyDonationRequestDto);

        bookApplyDonation.addBook(book);


        return ResponseEntity.ok().body(new MessageDto("책 나눔 신청이 완료되었습니다."));

    }


    public ResponseEntity<MessageDto> deleteBookApplyDonation(Long applyId) {
        BookApplyDonation bookApplyDonation = bookApplyDonationRepository.findById(applyId)
                .orElseThrow(()->new IllegalArgumentException("해당 신청이 존재하지 않습니다."));

        bookApplyDonationRepository.delete(bookApplyDonation);

        return ResponseEntity.ok().body(new MessageDto("책 나눔 신청이 취소되었습니다."));
    }

    public List<BookResponseDto> getDonationBooks(String bookStatus) {
        return bookRepository.findByBookStatus(bookStatus).stream()
                .map(BookResponseDto::new)
                .toList();
    }
}
