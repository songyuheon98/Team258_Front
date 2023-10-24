package com.example.team258.common.dto;

import com.example.team258.domain.donation.dto.BookDonationEventResponseDto;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookApplyDonationEventResultDto {

    private BookDonationEventResponseDto bookDonationEventResponseDto;
    private List<BookResponseDto> bookResponseDtos;
    private int totalPages;

    public BookApplyDonationEventResultDto(BookDonationEventResponseDto bookDonationEventResponseDto, List<BookResponseDto> bookResponseDtos, int totalPages) {
        this.bookDonationEventResponseDto = bookDonationEventResponseDto;
        this.bookResponseDtos = bookResponseDtos;
        this.totalPages = totalPages;
    }
}
