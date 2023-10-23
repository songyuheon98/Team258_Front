package com.example.team258.common.dto;


import com.example.team258.domain.donation.dto.BookDonationEventPageResponseDtoV3;
import lombok.Data;

@Data
public class DonationV3ServiceResultDto {
    private BookDonationEventPageResponseDtoV3 bookDonationEventPageResponseDtoV3;
    private int bookPages[];
    private int bookPageTotals[];


    public DonationV3ServiceResultDto(BookDonationEventPageResponseDtoV3 bookDonationEventPageResponseDtoV3, int[] bookPageTotalTemp, int[] bookPageTemp) {
        this.bookDonationEventPageResponseDtoV3 = bookDonationEventPageResponseDtoV3;
        this.bookPages = bookPageTemp;
        this.bookPageTotals = bookPageTotalTemp;
    }
}
