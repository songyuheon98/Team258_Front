package com.example.team258.repository;

import com.example.team258.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    Optional<BookCategory> findByCategoryName(String bookCategoryName);
    BookCategory findByBookCategoryName(String bookCategoryName);
}
