package com.example.team258.repository;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookRepository extends JpaRepository <Book,Long> {

    Page<Book> findAllByBookNameContaining(String keyword, Pageable pageable);

    @Query("SELECT b FROM book b WHERE b.bookCategory IN :bookCategories")
    Page<Book> findAllByCategories(List<BookCategory> bookCategories, Pageable pageable);


    @Query("SELECT b FROM book b WHERE b.bookCategory IN :bookCategories AND b.bookName like :keyword")
    Page<Book> findAllByCategoriesAndBookNameContaining(List<BookCategory> bookCategories, String keyword, Pageable pageable);
}
