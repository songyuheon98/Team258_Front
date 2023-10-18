package com.example.team258.dto;

import com.example.team258.entity.BookRent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BookRentResponseDto {
    private Long bookId;
    private String bookName;
    private LocalDateTime returnDate;

    public BookRentResponseDto(BookRent bookRent) {
        this.bookId = bookRent.getBook().getBookId();
        this.bookName = bookRent.getBook().getBookName();
        this.returnDate = bookRent.getReturnDate();
    }
}