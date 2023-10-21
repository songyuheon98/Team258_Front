package com.example.team258.common.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookResponsePageDto {
    private List<BookResponseDto> bookResponseDtos;
    private int totalPages;
    public BookResponsePageDto(List<BookResponseDto> bookResponseDtos, int totalPages){
        this.bookResponseDtos = bookResponseDtos;
        this.totalPages = totalPages;
    }

}
