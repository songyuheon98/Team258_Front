package com.example.team258.common.dto;

import com.example.team258.domain.admin.dto.AdminCategoriesResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class BooksCategoryPageResponseDto {
    private List<AdminCategoriesResponseDto> adminCategoriesResponseDtos;
    private int totalPages;

    public BooksCategoryPageResponseDto(List<AdminCategoriesResponseDto> adminCategoriesResponseDtos, int totalPages) {
        this.adminCategoriesResponseDtos = adminCategoriesResponseDtos;
        this.totalPages = totalPages;
    }
}
