package com.example.team258.dto;

import com.example.team258.entity.BookCategory;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminCategoriesResponseDto {
    private String bookCategoryName;
    private Long categoryId;

    public AdminCategoriesResponseDto(BookCategory bookCategory){
        this.bookCategoryName = bookCategory.getBookCategoryName();
        this.categoryId = bookCategory.getBookCategoryId();
    }
}


