package com.example.team258.dto;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.BookStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminBooksResponseDto {
    private Long bookId;
    private String bookAuthor;
    private LocalDateTime bookPublish;
    private BookStatusEnum bookStatus;
    private BookCategory bookCategory;

    public AdminBooksResponseDto(Book book) {
        this.bookId = book.getBookId();
        this.bookAuthor = book.getBookAuthor();
        this.bookPublish = book.getBookPublish();
        this.bookStatus = book.getBookStatus();
        this.bookCategory = book.getBookCategory();
    }
}