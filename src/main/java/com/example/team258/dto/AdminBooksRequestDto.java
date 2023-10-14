package com.example.team258.dto;

import com.example.team258.entity.BookStatusEnum;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdminBooksRequestDto {
    private String bookName;
    private String bookAuthor;
    private LocalDateTime bookPublish;
    private Long bookCategoryId;
    private BookStatusEnum bookStatus;
}
