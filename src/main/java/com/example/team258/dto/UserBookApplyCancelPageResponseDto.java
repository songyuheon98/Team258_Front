package com.example.team258.dto;

import com.example.team258.entity.User;
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
