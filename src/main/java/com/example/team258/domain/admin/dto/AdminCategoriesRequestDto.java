package com.example.team258.domain.admin.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategoriesRequestDto {
    private String bookCategoryName;
    private Long bookCategoryIsbnCode;
}