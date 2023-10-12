package com.example.team258.entity;

import com.example.team258.dto.BookApplyDonationRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "book_apply_donation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookApplyDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long applyId;

    @Column(name = "apply_date")
    private LocalDateTime applyDate;

    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true, mappedBy = "bookApplyDonation")
    private Book book;

    public BookApplyDonation(BookApplyDonationRequestDto bookApplyDonationRequestDto) {
        this.applyDate = bookApplyDonationRequestDto.getApplyDate();
    }

    public void addBook(Book book){
        this.book = book;
        book.addBookApplyDonation(this);
    }
}
