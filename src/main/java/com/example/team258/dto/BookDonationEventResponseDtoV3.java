package com.example.team258.dto;
import com.example.team258.entity.BookDonationEvent;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BookDonationEventResponseDtoV3 {
    private Long donationId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private List<BookResponseDto> bookResponseDtos;
    public BookDonationEventResponseDtoV3(BookDonationEvent t) {
        this.donationId = t.getDonationId();
        this.createdAt = t.getCreatedAt();
        this.closedAt = t.getClosedAt();
    }
}
