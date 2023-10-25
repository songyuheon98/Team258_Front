package com.example.team258.domain.donation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MessageAndDonationIdDto {
    private Long donationId;
    private String msg;
}
