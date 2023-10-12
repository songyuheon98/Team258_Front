package com.example.team258.dto;

import com.example.team258.entity.*;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BookResponseDto {
    private Long bookId;
    private String bookName;
    private String bookInfo;
    private String bookAuthor;
    private LocalDateTime bookPublish;
    private BookStatusEnum bookStatus;
    private BookApplyDonation bookApplyDonation;
    private BookRent bookRent;
    private List<BookReservation> bookReservations;
    private BookCategory bookCategory;

    public BookResponseDto(Book book){
        this.bookId = book.getBookId();
        this.bookName = book.getBookName();
        this.bookInfo = book.getBookInfo();
        this.bookAuthor = book.getBookAuthor();
        this.bookPublish = book.getBookPublish();
        this.bookStatus = book.getBookStatus();
        this.bookApplyDonation = book.getBookApplyDonation();
        this.bookRent = book.getBookRent();
        this.bookReservations = book.getBookReservations();
        this.bookCategory = book.getBookCategory();
    }
}
