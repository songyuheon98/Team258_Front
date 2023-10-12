package com.example.team258.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookApplyDonationRequestDto {
    private Long BookId;
    private LocalDateTime applyDate;
}
