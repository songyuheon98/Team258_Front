package com.example.team258.dto;

import lombok.Data;

@Data
public class BookDonationEventIncludeBookResaponseDto {
    private BookDonationEventResponseDto bookDonationEventResponseDto;
    private BookResponseDto bookResponseDto;

    public BookDonationEventIncludeBookResaponseDto(BookDonationEventResponseDto bookDonationEventResponseDto, BookResponseDto bookResponseDto){
        this.bookDonationEventResponseDto = bookDonationEventResponseDto;
        this.bookResponseDto = bookResponseDto;
    }
}
