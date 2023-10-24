package com.example.team258.domain.donation.service;

import com.example.team258.common.dto.BookApplyDonationEventResultDto;
import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.dto.DonationV3ServiceResultDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.User;
import com.example.team258.common.jwt.SecurityUtil;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.donation.dto.*;
import com.example.team258.domain.donation.entity.BookApplyDonation;
import com.example.team258.domain.donation.entity.BookDonationEvent;
import com.example.team258.domain.donation.entity.QBookDonationEvent;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookDonationEventService {
    private final BookDonationEventRepository bookDonationEventRepository;
    private final BookRepository bookRepository;
    private final BookApplyDonationRepository bookApplyDonationRepository;

    public DonationV3ServiceResultDto donationV3Service(int[] bookPage, int bookPagesize, PageRequest eventPageRequest) {

        /**
         * 이벤트 페이징 리스트
         */
        BookDonationEventPageResponseDtoV3 bookDonationEventPageResponseDtoV3 = getDonationEventV3(eventPageRequest);

        int eventListSize = bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().size();
        int bookPageTemp[] = new int[eventListSize];
        int bookPageTotalTemp[] = new int[eventListSize];

        for (int i = 0; i < bookPage.length; i++)
            bookPageTemp[i] = bookPage[i];

        /**
         * 이벤트에 대한 도서들 페이징 리스트
         */
        List<Page<Book>> bookPages = new ArrayList<>();
        for (int i = 0; i < eventListSize; i++) {
            PageRequest bookPageRequest = PageRequest.of(bookPageTemp[i], bookPagesize);
            bookPages.add(bookRepository.findBooksNoStatusByDonationId(
                    bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().get(i).getDonationId(),
                    bookPageRequest));
        }
        /**
         * 이벤트에 대한 도서들 페이징 리스트를 이벤트에 추가
         */
        for (int i = 0; i < bookPages.size(); i++) {
            bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().get(i)
                    .setBookResponseDtos(bookPages.get(i).stream().map(BookResponseDto::new).toList());
            bookPageTotalTemp[i] = bookPages.get(i).getTotalPages();
        }

        return new DonationV3ServiceResultDto(bookDonationEventPageResponseDtoV3, bookPageTotalTemp,bookPageTemp);

    }

    @Transactional
    public MessageDto createDonationEvent(BookDonationEventRequestDto bookDonationEventRequestDto) {
        BookDonationEvent bookDonationEvent = new BookDonationEvent(bookDonationEventRequestDto);
        bookDonationEventRepository.save(bookDonationEvent);
        return new MessageDto("이벤트추가가 완료되었습니다");
    }


    @Transactional
    public MessageDto updateDonationEvent(Long donationId, BookDonationEventRequestDto bookDonationEventRequestDto) {
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        bookDonationEvent.update(bookDonationEventRequestDto);
        return new MessageDto("이벤트 수정이 완료되었습니다");
    }

    @Transactional
    public MessageDto deleteDonationEvent(Long donationId) {
        User user = SecurityUtil.getPrincipal().get();
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            return new MessageDto("관리자만 이벤트를 삭제할 수 있습니다.");
        }
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        /**
         * 연관 관계 삭제
         * 도서와 나눔 이벤트 간의 연관 관계 삭제
         */
        int bookSize = bookDonationEvent.getBooks().size();
        for (int i = bookSize - 1; i >= 0; i--) {
            bookDonationEvent.getBooks().get(i).changeStatus(BookStatusEnum.POSSIBLE);
            bookDonationEvent.removeBook(bookDonationEvent.getBooks().get(i));
        }

        /**
         * 도서와 나눔 신청 간의 연관 관계 삭제
         */
        int applysize = bookDonationEvent.getBookApplyDonations().size();
        for (int i = applysize - 1; i >= 0; i--) {
            Book book = bookRepository.findById(bookDonationEvent.getBookApplyDonations().get(i).getBook().getBookId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 책이 존재하지 않습니다.")
            );
            BookApplyDonation bookApplyDonation = bookApplyDonationRepository.findById(bookDonationEvent.getBookApplyDonations().get(i).getApplyId()).orElseThrow(
                    () -> new IllegalArgumentException("해당 신청이 존재하지 않습니다.")
            );
            bookApplyDonation.removeBook(book);
            int a;
        }

        bookDonationEventRepository.delete(bookDonationEvent);
        return new MessageDto("이벤트 삭제가 완료되었습니다");
    }

    public List<BookDonationEventResponseDto> getDonationEvent() {
        return bookDonationEventRepository.findAll().stream()
                .map(BookDonationEventResponseDto::new)
                .toList();
    }

    public BookDonationEventPageResponseDto getDonationEventV2(Pageable pageable) {
         Page<BookDonationEvent> bookDonationEvents =bookDonationEventRepository.findAll(pageable);
         int totalPages = bookDonationEvents.getTotalPages();
         List<BookDonationEventResponseDto> bookDonationEventResponseDtos = bookDonationEvents.stream().map(BookDonationEventResponseDto::new).toList();

         return new BookDonationEventPageResponseDto(bookDonationEventResponseDtos, totalPages);
    }


    public BookDonationEventPageResponseDtoV3 getDonationEventV3(Pageable pageable) {
        Page<BookDonationEvent> bookDonationEvents =bookDonationEventRepository.findAll(pageable);
        int totalPages = bookDonationEvents.getTotalPages();
        List<BookDonationEventResponseDtoV3> bookDonationEventResponseDtos = bookDonationEvents.stream().map((BookDonationEvent t) -> new BookDonationEventResponseDtoV3(t)).toList();

        return new BookDonationEventPageResponseDtoV3(bookDonationEventResponseDtos, totalPages);
    }


    public BookDonationEventOnlyPageResponseDto getDonationEventOnlyV2(PageRequest pageRequest) {
        Page<BookDonationEvent> bookDonationEvents =bookDonationEventRepository.findAll(pageRequest);
        int totalPages = bookDonationEvents.getTotalPages();
        List<BookDonationEventOnlyResponseDto> bookDonationEventResponseDtos = bookDonationEvents.stream().map(BookDonationEventOnlyResponseDto::new).toList();
        return new BookDonationEventOnlyPageResponseDto(bookDonationEventResponseDtos, totalPages);

    }

    public BookDonationEventOnlyPageResponseDto getDonationEventOnlyV3(PageRequest pageRequest, Long donationId, LocalDate eventStartDate, LocalDate eventEndDate) {
        QBookDonationEvent qBookDonationEvent = QBookDonationEvent.bookDonationEvent;
        BooleanBuilder builder = new BooleanBuilder();

        if(donationId!=null)
            builder.and(qBookDonationEvent.donationId.eq(donationId));
        if(eventStartDate!=null)
            builder.and(qBookDonationEvent.createdAt.after(eventStartDate.atStartOfDay()));
        if(eventEndDate!=null)
            builder.and(qBookDonationEvent.closedAt.before(eventEndDate.atStartOfDay()));

        Page<BookDonationEvent> bookDonationEvents =bookDonationEventRepository.findAll(builder,pageRequest);
        int totalPages = bookDonationEvents.getTotalPages();
        List<BookDonationEventOnlyResponseDto> bookDonationEventResponseDtos = bookDonationEvents.stream().map(BookDonationEventOnlyResponseDto::new).toList();

        if(bookDonationEventResponseDtos.size()!=0)
            return new BookDonationEventOnlyPageResponseDto(bookDonationEventResponseDtos, totalPages);


        return new BookDonationEventOnlyPageResponseDto(List.of(
                new BookDonationEventOnlyResponseDto(-1L,LocalDateTime.parse("0001-01-01T00:00:00"),
                LocalDateTime.parse("0001-01-01T00:00:00"))), 1);

    }
    public List<BookDonationEventResponseDto> getDonationEventPage() {
        return bookDonationEventRepository.findAll().stream()
                .map(BookDonationEventResponseDto::new)
                .toList();
    }

    @Transactional
    public MessageDto settingDonationEvent(BookDonationSettingRequestDto bookDonationSettingRequestDto) {
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(bookDonationSettingRequestDto.getDonationId()).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        List<Book> books =bookDonationSettingRequestDto.getBookIds().stream()
                .map(bookId -> bookRepository.findById(bookId).orElseThrow(
                        () -> new IllegalArgumentException("해당 책이 존재하지 않습니다.")
                ))
                .toList();

        books.forEach(book -> {
            bookDonationEvent.addBook(book);
            book.changeStatus(BookStatusEnum.DONATION);
        });

        return new MessageDto("이벤트 설정이 완료되었습니다");
    }

    @Transactional
    public MessageDto settingCancelDonationEvent(BookDonationSettingCancelRequestDto bookDonationSettingCancelRequestDto) {
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(bookDonationSettingCancelRequestDto.getDonationId()).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        Book book =bookRepository.findById(bookDonationSettingCancelRequestDto.getBookId()).orElseThrow(
                () -> new IllegalArgumentException("해당 책이 존재하지 않습니다.")
        );
        bookDonationEvent.removeBook(book);
        book.changeStatus(BookStatusEnum.POSSIBLE);
        return new MessageDto("해당 도서가 이벤트에서 삭제 완료되었습니다");
    }

    @Transactional
    public MessageDto endDonationEvent(Long donationId) {
        User user = SecurityUtil.getPrincipal().get();
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            return new MessageDto("관리자만 이벤트를 종료할 수 있습니다.");
        }
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        bookDonationEventRepository.delete(bookDonationEvent);
        return new MessageDto("이벤트 종료가 완료되었습니다");
    }

    public BookApplyDonationEventResultDto bookApplyDonationEventPageV2Result(PageRequest pageRequest, Long donationId) {

        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        Page<Book> books = bookRepository.findBooksByDonationId(donationId,BookStatusEnum.DONATION,pageRequest);

        List<BookResponseDto> bookResponseDtos = books.stream()
                .map(BookResponseDto::new)
                .toList();

        return new BookApplyDonationEventResultDto(new BookDonationEventResponseDto(bookDonationEvent), bookResponseDtos, books.getTotalPages());
    }
}
