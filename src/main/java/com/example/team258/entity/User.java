package com.example.team258.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User extends Timestamped{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRoleEnum role;


    @OneToMany(orphanRemoval = true,mappedBy = "user")
    private List<BookReservation> bookReservations = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<BookRent> bookRents = new ArrayList<>();

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private List<BookApplyDonation> bookApplyDonations = new ArrayList<>();


    public User(String username, String password, UserRoleEnum role) {
        this.username=username;
        this.password=password;
        this.role=role;
    }

    //public void addBookReservation(BookReservation bookReservation){
    //    this.bookReservations.add(bookReservation);
    //    bookReservation.addUser(this);
    //}

}
