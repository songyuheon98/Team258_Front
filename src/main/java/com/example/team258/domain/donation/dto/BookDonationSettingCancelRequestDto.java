package com.example.team258.domain.donation.dto;

import lombok.Data;

@Data
public class BookDonationSettingCancelRequestDto {
    private Long donationId;
    private Long bookId;
}
