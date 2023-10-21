package com.example.team258.domain.donation.dto;

import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.User;
import lombok.Data;

import java.util.List;

@Data
public class UserBookApplyCancelPageResponseDto {
    private Long userId;
    private List<BookResponseDto> bookResponseDto;
    private List<Long> BookApplyId;

    public UserBookApplyCancelPageResponseDto(User user){
        this.userId = user.getUserId();
        this.bookResponseDto = user.getBookApplyDonations().stream()
                .map(bookApplyDonation -> new BookResponseDto(bookApplyDonation.getBook()))
                .toList();
        this.BookApplyId = user.getBookApplyDonations().stream()
                .map(bookApplyDonation -> bookApplyDonation.getApplyId())
                .toList();
    }
}
