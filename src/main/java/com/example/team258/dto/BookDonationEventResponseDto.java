package com.example.team258.dto;

import com.example.team258.entity.BookDonationEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BookDonationEventResponseDto {
    private Long donatoinId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    public BookDonationEventResponseDto(BookDonationEvent bookDonationEvent){
        this.donatoinId = bookDonationEvent.getDonatoinId();
        this.createdAt = bookDonationEvent.getCreatedAt();
        this.closedAt = bookDonationEvent.getClosedAt();
    }
}
