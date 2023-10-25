package com.example.team258.domain.donation.dto;


import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDonationEventRequestDto {
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

}
