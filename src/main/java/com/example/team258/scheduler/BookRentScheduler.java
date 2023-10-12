package com.example.team258.scheduler;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookRent;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.repository.BookRentRepository;
import com.example.team258.repository.BookRepository;
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
    private final BookRepository bookRepository;

    @Transactional
    @Scheduled(cron = "0 0 01 * * ?") // 매일 새벽 1시
    public void autoDeleteRental() {
        List<BookRent> bookRentList = bookRentRepository.findAll();
        for (BookRent bookRent : bookRentList) {
            if (bookRent.getReturnDate().isAfter(LocalDateTime.now())) {
                Book book = bookRepository.findByBookRent(bookRent)
                        .orElseThrow(()-> new IllegalArgumentException("해당하는 book이 없습니다"));
                book.changeStatus(BookStatusEnum.POSSIBLE);
                bookRentRepository.deleteById(bookRent.getBookRentId()); //이거만 삭제해도 되는지 확인필요
            }
        }
    }
}
