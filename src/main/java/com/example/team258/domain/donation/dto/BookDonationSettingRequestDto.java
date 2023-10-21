package com.example.team258.domain.donation.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDonationSettingRequestDto {
    private Long donationId;
    private List<Long> bookIds;
}
