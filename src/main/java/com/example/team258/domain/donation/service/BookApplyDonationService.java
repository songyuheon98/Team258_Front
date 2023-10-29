package com.example.team258.domain.donation.service;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.BookResponsePageDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.QBook;
import com.example.team258.common.entity.User;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.domain.donation.dto.BookApplyDonationRequestDto;
import com.example.team258.domain.donation.dto.BookApplyDonationResponseDto;
import com.example.team258.domain.donation.dto.UserBookApplyCancelPageResponseDto;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.donation.repository.BookApplyDonationRepository;
import com.example.team258.domain.donation.repository.BookDonationEventRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Semaphore;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookApplyDonationService {

    private final BookRepository bookRepository;
    private final BookDonationEventRepository bookDonationEventRepository;
    private final BookApplyDonationRepository bookApplyDonationRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto createBookApplyDonation(BookApplyDonationRequestDto bookApplyDonationRequestDto) {

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
            return new MessageDto("이미 누군가 먼저 신청했습니다.");
        }
        /**
         * 나눔 이벤트 시간이 아닐때
         */
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(bookApplyDonationRequestDto.getDonationId())
                .orElseThrow(()->new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));

        if(LocalDateTime.now().isBefore(bookDonationEvent.getCreatedAt()) ||
                LocalDateTime.now().isAfter( bookDonationEvent.getClosedAt())){
            return new MessageDto("책 나눔 이벤트 기간이 아닙니다.");
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
        book.changeStatus(BookStatusEnum.SOLD_OUT);
        /**
         * book의 상태 변경
         */
        return new MessageDto("책 나눔 신청이 완료되었습니다.");
    }
    @Transactional
    public MessageDto createBookApplyDonationKafka(BookApplyDonationRequestDto bookApplyDonationRequestDto,Long userId) {

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
            return new MessageDto("이미 누군가 먼저 신청했습니다.");
        }
        /**
         * 나눔 이벤트 시간이 아닐때
         */
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(bookApplyDonationRequestDto.getDonationId())
                .orElseThrow(()->new IllegalArgumentException("해당 이벤트가 존재하지 않습니다."));

        if(LocalDateTime.now().isBefore(bookDonationEvent.getCreatedAt()) ||
                LocalDateTime.now().isAfter( bookDonationEvent.getClosedAt())){
            return new MessageDto("책 나눔 이벤트 기간이 아닙니다.");
        }
        /**
         * 신청자가 도서관 사용자가 아닐때
         */
        User user = userRepository.findById(userId).orElseThrow(
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
        book.changeStatus(BookStatusEnum.SOLD_OUT);
        /**
         * book의 상태 변경
         */
        return new MessageDto("책 나눔 신청이 완료되었습니다.");
    }

    @Transactional
    public MessageDto deleteBookApplyDonation(Long applyId) {
        BookApplyDonation bookApplyDonation = bookApplyDonationRepository.findById(applyId)
                .orElseThrow(()->new IllegalArgumentException("해당 신청이 존재하지 않습니다."));

        /**
         * 책 상태 Donation으로 수정
         */
        bookApplyDonation.getBook().changeStatus(BookStatusEnum.DONATION);

        /**
         * 연관관계 해제 편의 메소드 사용
         */
        bookApplyDonation.removeBook(bookApplyDonation.getBook());

        /**
         * 책나눔 신청 취소
         */
        bookApplyDonationRepository.delete(bookApplyDonation);

        return new MessageDto("책 나눔 신청이 취소되었습니다.");
    }

    public List<BookResponseDto> getDonationBooks(BookStatusEnum bookStatus) {

        List<BookResponseDto> bookResponseDtos= bookRepository.findByBookStatus(bookStatus).stream()
                .map(BookResponseDto::new)
                .toList();
        return bookResponseDtos;
    }
    public BookResponsePageDto getDonationBooksV2(BookStatusEnum bookStatus, Pageable pageable) {
        Page<Book> pageBooks = bookRepository.findPageByBookStatus(bookStatus,pageable);
        List<BookResponseDto> bookResponseDtos= pageBooks.stream()
                .map(BookResponseDto::new)
                .toList();

        return new BookResponsePageDto(bookResponseDtos, pageBooks.getTotalPages());
    }
    public BookResponsePageDto getDonationBooksV3(BookStatusEnum bookStatusEnum, PageRequest pageRequest, BookResponseDto bookResponseDto) {
        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        if(bookResponseDto.getBookId()!=null && !bookResponseDto.getBookId().equals(""))
            builder.and(qBook.bookId.eq(bookResponseDto.getBookId()));

        if(bookResponseDto.getBookName()!=null && !bookResponseDto.getBookName().equals(""))
            builder.and(qBook.bookName.contains(bookResponseDto.getBookName()));
        if(bookResponseDto.getBookAuthor()!=null && !bookResponseDto.getBookAuthor().equals(""))
            builder.and(qBook.bookAuthor.contains(bookResponseDto.getBookAuthor()));
        if(bookResponseDto.getBookPublish()!=null && !bookResponseDto.getBookPublish().equals(""))
            builder.and(qBook.bookPublish.contains(bookResponseDto.getBookPublish()));
        builder.and(qBook.bookStatus.eq(bookStatusEnum));

        Page<Book> pageBooks = bookRepository.findAll(builder, pageRequest);

        List<BookResponseDto> bookResponseDtos= pageBooks.stream()
                .map(BookResponseDto::new)
                .toList();

        return new BookResponsePageDto(bookResponseDtos, pageBooks.getTotalPages());
    }

    public List<BookApplyDonationResponseDto> getBookApplyDonations() {
        return bookApplyDonationRepository.findAll().stream()
                .map(bookApplyDonation -> new BookApplyDonationResponseDto(bookApplyDonation))
                .toList();
    }


    public UserBookApplyCancelPageResponseDto getDonationBooksCancel() {
        Long userId = SecurityUtil.getPrincipal().get().getUserId();
        User user = userRepository.findFetchJoinById(userId).orElseThrow(
                ()->new IllegalArgumentException("해당 사용자가 존재하지 않습니다.")
        );
        return new UserBookApplyCancelPageResponseDto(user);
    }



}

