package com.example.team258.dto;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookDonationEventRequestDto {
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

}
