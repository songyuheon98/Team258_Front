package com.example.team258.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "book_apply_donation")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookApplyDonation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long applyId;

    @OneToOne(fetch = FetchType.LAZY,orphanRemoval = true, mappedBy = "bookApplyDonation")
    private Book book;
}
