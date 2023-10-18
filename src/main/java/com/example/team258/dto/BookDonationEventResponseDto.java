package com.example.team258.dto;

import com.example.team258.entity.BookDonationEvent;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BookDonationEventResponseDto {
    private Long donationId;
    private LocalDateTime createdAt;
    private LocalDateTime closedAt;
    private List<BookResponseDto> bookResponseDtos;

    public BookDonationEventResponseDto(BookDonationEvent bookDonationEvent){
        this.donationId = bookDonationEvent.getDonationId();
        this.createdAt = bookDonationEvent.getCreatedAt();
        this.closedAt = bookDonationEvent.getClosedAt();
        this.bookResponseDtos = bookDonationEvent.getBooks().stream().map(BookResponseDto::new).toList();
    }
}
