package com.example.team258.domain.donation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDonationSettingRequestDto {
    private Long donationId;
    private List<Long> bookIds;
}
