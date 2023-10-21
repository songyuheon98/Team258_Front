package com.example.team258.domain.donation.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDonationEventPageResponseDto {
    private List<BookDonationEventResponseDto> bookDonationEventResponseDtos;
    private int totalPages;

    public BookDonationEventPageResponseDto(List<BookDonationEventResponseDto> bookDonationEventResponseDtos, int totalPages) {
        this.bookDonationEventResponseDtos = bookDonationEventResponseDtos;
        this.totalPages = totalPages;
    }

}
