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
    private String bookName;
    //private String bookInfo;
    private String bookAuthor;
    //private LocalDateTime bookPublish;
    private Long bookPublish;
    private BookStatusEnum bookStatus;
    //private BookCategory bookCategory;
    private Long bookCategoryId;

    public AdminBooksResponseDto(Book book) {
        this.bookId = book.getBookId();
        this.bookName = book.getBookName();
        //this.bookInfo = book.getBookInfo();
        this.bookAuthor = book.getBookAuthor();
        this.bookPublish = book.getBookPublish();
        this.bookStatus = book.getBookStatus();
        this.bookCategoryId = book.getBookCategory().getBookCategoryId();
    }
}