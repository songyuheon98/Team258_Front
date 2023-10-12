package com.example.team258.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "book_category")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class BookCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="book_category_id")
    private Long bookCategoryId;

    @OneToMany(orphanRemoval = true, mappedBy = "bookCategory")
    private List<Book> books = new ArrayList<>();

}
