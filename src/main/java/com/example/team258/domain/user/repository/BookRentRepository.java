package com.example.team258.domain.user.repository;

import com.example.team258.domain.user.entity.BookRent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRentRepository extends JpaRepository<BookRent, Long> {
}
