package com.example.team258.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookDonationSettingCancelRequestDto {
    private Long donationId;
    private Long bookId;
}
