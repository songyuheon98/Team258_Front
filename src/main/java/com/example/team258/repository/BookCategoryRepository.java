package com.example.team258.repository;

import com.example.team258.entity.BookCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {
    BookCategory findByBookCategoryName(String bookCategoryName);
}
