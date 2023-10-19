package com.example.team258.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDonationEventPageResponseDtoV3 {
    private List<BookDonationEventResponseDtoV3> bookDonationEventResponseDtoV3;
    private int totalPages;

    public BookDonationEventPageResponseDtoV3(List<BookDonationEventResponseDtoV3> bookDonationEventResponseDtoV3s, int totalPages) {
        this.bookDonationEventResponseDtoV3 = bookDonationEventResponseDtoV3s;
        this.totalPages = totalPages;
    }
}
