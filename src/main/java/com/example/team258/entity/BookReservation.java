package com.example.team258.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "book_reservation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_reservation_id")
    private Long bookReservationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    public void addUser(User user){
        this.user=user;
    }
}
