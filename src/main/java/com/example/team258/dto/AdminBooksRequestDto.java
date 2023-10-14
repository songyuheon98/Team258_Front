package com.example.team258.dto;

import com.example.team258.entity.BookStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdminBooksRequestDto {
    private String bookName;
    private String bookAuthor;
    private long bookPublish;
    private Long bookCategoryId;
    private BookStatusEnum bookStatus;
}
