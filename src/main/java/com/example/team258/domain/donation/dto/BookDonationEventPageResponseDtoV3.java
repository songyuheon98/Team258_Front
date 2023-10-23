package com.example.team258.domain.donation.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BookDonationEventPageResponseDtoV3 {
    private List<BookDonationEventResponseDtoV3> bookDonationEventResponseDtoV3;
    private int totalPages;

    public BookDonationEventPageResponseDtoV3(List<BookDonationEventResponseDtoV3> bookDonationEventResponseDtoV3s, int totalPages) {
        this.bookDonationEventResponseDtoV3 = bookDonationEventResponseDtoV3s;
        this.totalPages = totalPages;
    }
}
