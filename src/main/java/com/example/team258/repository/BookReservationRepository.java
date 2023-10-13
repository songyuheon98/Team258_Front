package com.example.team258.repository;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookReservation;
import com.example.team258.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    Optional<BookReservation> findByUserAndBook(User user, Book book);
}
