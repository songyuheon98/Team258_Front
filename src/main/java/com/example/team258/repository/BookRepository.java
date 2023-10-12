package com.example.team258.repository;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByBookRent(BookRent bookRent);
}
