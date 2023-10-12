package com.example.team258.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdminBooksRequestDto {
    private String bookName;
    private String bookInfo;
    private String bookAuthor;
    private LocalDateTime bookPublish;
    private Long categoryId;
}
