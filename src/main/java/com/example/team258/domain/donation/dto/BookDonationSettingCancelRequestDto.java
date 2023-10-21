package com.example.team258.domain.donation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDonationSettingCancelRequestDto {
    private Long donationId;
    private Long bookId;
}
