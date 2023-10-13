package com.example.team258.dto;

import com.example.team258.entity.BookStatusEnum;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdminBooksRequestDto {
    private String bookName;
    private String bookInfo;
    private String bookAuthor;
    private LocalDateTime bookPublish;
    private Long bookCategoryId;
    private BookStatusEnum bookStatus;
}
