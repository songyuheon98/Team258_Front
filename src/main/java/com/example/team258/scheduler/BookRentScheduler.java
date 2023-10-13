package com.example.team258.scheduler;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookRent;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.entity.User;
import com.example.team258.repository.BookRentRepository;
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

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?") // 매일 새벽 00시
    public void autoDeleteRental() {
        List<BookRent> bookRentList = bookRentRepository.findAll();
        for (BookRent bookRent : bookRentList) {
            if (bookRent.getReturnDate().isAfter(LocalDateTime.now())) {
                Book book = bookRent.getBook();
                bookRentRepository.deleteById(bookRent.getBookRentId()); //확인필요

                //예약자가 있는 경우 첫번째 예약자가 바로 대출
                if (!book.getBookReservations().isEmpty()) {
                    User rsvUser = book.getBookReservations().get(0).getUser();
                    book.getBookReservations().remove(0);
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
