package com.example.team258.repository;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepositoryCustom {
    Page<Book> findAllByCategoriesAndBookNameContaining2(@Param("bookCategories") List<BookCategory> bookCategories, @Param("keyword")String keyword, Pageable pageable);
}
