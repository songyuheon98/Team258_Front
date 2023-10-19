package com.example.team258.dto;

import com.example.team258.entity.BookDonationEvent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookDonationEventOnlyResponseDto {
    private Long donationId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public BookDonationEventOnlyResponseDto(BookDonationEvent bookDonationEvent){
        this.donationId = bookDonationEvent.getDonationId();
        this.createdAt = bookDonationEvent.getCreatedAt();
        this.closedAt = bookDonationEvent.getClosedAt();
    }
}
