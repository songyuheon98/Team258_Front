package com.example.team258.domain.user.dto;

import com.example.team258.domain.user.entity.BookReservation;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookReservationResponseDto {
    private Long bookId;
    private String bookName;

    public BookReservationResponseDto(BookReservation bookReservation) {
        this.bookId = bookReservation.getBook().getBookId();
        this.bookName = bookReservation.getBook().getBookName();
    }
}
