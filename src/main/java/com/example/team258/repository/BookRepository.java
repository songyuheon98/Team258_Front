package com.example.team258.repository;

import com.example.team258.entity.Book;
import com.example.team258.entity.BookStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByBookStatus(BookStatusEnum bookStatus);
}
