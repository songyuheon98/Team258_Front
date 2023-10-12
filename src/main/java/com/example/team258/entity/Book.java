package com.example.team258.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    //public void addBookRent(BookRent bookRent){
    //    this.bookRent = bookRent;
    //    bookRent.addBook(this);
    //}


}
