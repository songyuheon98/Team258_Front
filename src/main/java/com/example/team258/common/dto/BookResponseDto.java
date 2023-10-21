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

    public BookResponseDto(Long bookId,String bookName, String author, String publish) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookAuthor = author;
        this.bookPublish = publish;
    }
}
