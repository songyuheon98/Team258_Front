package com.example.team258.domain.donation.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookDonationEventOnlyPageResponseDto {
    private List<BookDonationEventOnlyResponseDto> bookDonationEventOnlyResponseDtos;
    private int totalpages;

    public BookDonationEventOnlyPageResponseDto(List<BookDonationEventOnlyResponseDto> bookDonationEventOnlyResponseDtos, int totalpages) {
        this.bookDonationEventOnlyResponseDtos = bookDonationEventOnlyResponseDtos;
        this.totalpages = totalpages;
    }
}
