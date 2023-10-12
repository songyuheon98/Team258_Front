package com.example.team258.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "book_donation_event")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookDonationEvent {
    @Id @GeneratedValue
    @Column(name="donation_id")
    private Long donatoinId;

    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Column(name="closed_at")
    private LocalDateTime closedAt;

    // 단 방향
    @OneToMany(fetch = FetchType.LAZY,orphanRemoval = true)
    @JoinColumn(name="donation_id")
    private List<BookApplyDonation> bookApplyDonations;
}
