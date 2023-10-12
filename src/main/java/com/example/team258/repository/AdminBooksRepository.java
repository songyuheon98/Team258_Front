package com.example.team258.repository;

import com.example.team258.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminBooksRepository extends JpaRepository<Book, Long> {
}
