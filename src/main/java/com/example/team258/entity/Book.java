package com.example.team258.entity;

import com.example.team258.dto.AdminBooksRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "book")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_id")
    private Long bookId;

    @Column(name = "book_name", nullable = false)
    private String bookName;

    @Column(name = "book_info", nullable = false)
    private String bookInfo;

    @Column(name = "book_author", nullable = false)
    private String bookAuthor;

    @Column(name = "book_publish", nullable = false)
    private LocalDateTime bookPublish;

    @Column(name = "book_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private BookStatusEnum bookStatus;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="apply_id")
    private BookApplyDonation bookApplyDonation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="rent_id")
    private BookRent bookRent;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="book_reservation_id")
    private List<BookReservation> bookReservations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="book_category_id")
    private BookCategory bookCategory;



    public Book(AdminBooksRequestDto requestDto, BookCategory bookCategory){
        this.bookName = requestDto.getBookName();
        this.bookInfo = requestDto.getBookInfo();
        this.bookAuthor = requestDto.getBookAuthor();
        this.bookPublish = requestDto.getBookPublish();
        this.bookCategory = bookCategory;
    }

    //public void addBookRent(BookRent bookRent){
    //    this.bookRent = bookRent;
    //    bookRent.addBook(this);
    //}

    public void addBookApplyDonation(BookApplyDonation bookApplyDonation){
        this.bookApplyDonation = bookApplyDonation;
    }


}
