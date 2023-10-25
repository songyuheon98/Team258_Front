package com.example.team258.common.dto;

import com.example.team258.domain.admin.dto.AdminBooksResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class BooksPageResponseDto {
    private List<AdminBooksResponseDto> adminBooksResponseDtos;
    private int totalPages;

    public BooksPageResponseDto(List<AdminBooksResponseDto> adminBooksResponseDtos, int totalPages) {
        this.adminBooksResponseDtos = adminBooksResponseDtos;
        this.totalPages = totalPages;
    }
}