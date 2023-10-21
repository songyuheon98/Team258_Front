package com.example.team258.common.dto;

import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookResponseDto {
    private Long bookId;
    private String bookName;
    private String bookAuthor;
    private String bookPublish;
    private BookStatusEnum bookStatus;

    public BookResponseDto(Book book){
        this.bookId = book.getBookId();
        this.bookName = book.getBookName();
        this.bookAuthor = book.getBookAuthor();
        this.bookPublish = book.getBookPublish();
        this.bookStatus = book.getBookStatus();
    }
}
