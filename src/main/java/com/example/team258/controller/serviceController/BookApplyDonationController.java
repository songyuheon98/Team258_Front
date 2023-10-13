package com.example.team258.controller.serviceController;

import com.example.team258.dto.BookApplyDonationRequestDto;
import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.BookResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.service.BookApplyDonationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/bookApplyDonation")
@RequiredArgsConstructor
public class BookApplyDonationController {

    private final BookApplyDonationService bookApplyDonationService;
    @PostMapping("/{donationId}")
    public ResponseEntity<MessageDto> createBookApplyDonation(@PathVariable Long donationId,@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto){
        return bookApplyDonationService.createBookApplyDonation(donationId,bookApplyDonationRequestDto);
    }


    @DeleteMapping("/{donationId}")
    public ResponseEntity<MessageDto> deleteBookApplyDonation(@PathVariable Long applyId){
        return bookApplyDonationService.deleteBookApplyDonation(applyId);
    }

    @GetMapping("/books")
    public List<BookResponseDto> getDonationBooks(@RequestParam String booksStatus){
        return bookApplyDonationService.getDonationBooks(booksStatus);
    }

}

