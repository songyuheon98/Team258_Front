package com.example.team258.domain.user.repository;

import com.example.team258.common.entity.Book;
import com.example.team258.domain.user.entity.BookReservation;
import com.example.team258.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookReservationRepository extends JpaRepository<BookReservation, Long> {
    Optional<BookReservation> findByUserAndBook(User user, Book book);
}
