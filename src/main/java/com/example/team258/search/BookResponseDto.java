package com.example.team258.search;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookStatusEnum;

import java.time.LocalDateTime;

public class BookResponseDto {
//    "bookId":1L,
//    "bookName":"bookName",
//    "bookInfo":"bookInfo",
//    "bookAuthor":"bookAuthor",
//    "bookPublish":"2022-20-20",
//    "bookStatus":'possible',
//    "bookReservations":{},
//    "bookRents":{},
//    "bookApplyDonatoins":{},
//    ”bookCategory_name” : “Sci-fi”
    private Long bookId;
    private String bookName;
    private String bookInfo;
    private String bookAuthor;
    private LocalDateTime bookPublish;
    private BookStatusEnum bookStatus;
    private String bookCategoryName;

    public BookResponseDto(Book book){
        this.bookId = book.getBookId();
        this.bookName = book.getBookName();
        this.bookInfo = book.getBookInfo();
        this.bookAuthor = book.getBookAuthor();
        this.bookPublish = book.getBookPublish();
        this.bookStatus = book.getBookStatus();
        this.bookCategoryName = book.getBookCategory().getCategoryName();
    }

}
