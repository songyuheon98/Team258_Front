package com.example.team258.scheduler;

import com.example.team258.entity.*;
import com.example.team258.repository.BookRentRepository;
import com.example.team258.repository.BookReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookRentScheduler {

    private final BookRentRepository bookRentRepository;
    private final BookReservationRepository bookReservationRepository;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 새벽 00시
    public void autoDeleteRental() {
        List<BookRent> bookRentList = bookRentRepository.findAll();
        for (BookRent bookRent : bookRentList) {
            if (bookRent.getReturnDate().isAfter(LocalDateTime.now())) {
                Book book = bookRent.getBook();
                book.deleteRental();
                bookRentRepository.deleteById(bookRent.getBookRentId()); //확인필요

                //예약자가 있는 경우 첫번째 예약자가 바로 대출
                if (!book.getBookReservations().isEmpty()) {
                    User rsvUser = book.getBookReservations().get(0).getUser();
                    BookReservation bookReservation = book.getBookReservations().get(0);
                    bookReservationRepository.deleteById(bookReservation.getBookReservationId());


                    BookRent bookRentRsv = bookRentRepository.save(new BookRent(book));
                    book.addBookRent(bookRentRsv);
                    rsvUser.addBookRent(bookRentRsv);
                } else {
                    book.changeStatus(BookStatusEnum.POSSIBLE);
                }
            }
        }
    }
}
