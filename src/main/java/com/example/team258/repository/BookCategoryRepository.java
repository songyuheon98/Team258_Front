package com.example.team258.repository;

import com.example.team258.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCategoryRepository extends JpaRepository<BookCategory, Long> {

    BookCategory findByBookCategoryName(String bookCategoryName);

    Page<BookCategory> findAll(Specification<BookCategory> spec, Pageable pageable);
}
