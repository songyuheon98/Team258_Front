package com.example.team258.controller.serviceController;

import com.example.team258.dto.*;
import com.example.team258.entity.BookApplyDonation;
import com.example.team258.entity.BookStatusEnum;
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
    @PostMapping
    public ResponseEntity<MessageDto> createBookApplyDonation(@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto){
        return bookApplyDonationService.createBookApplyDonation(bookApplyDonationRequestDto);
    }

    @DeleteMapping("/{applyId}")
    public ResponseEntity<MessageDto> deleteBookApplyDonation(@PathVariable Long applyId){
        return bookApplyDonationService.deleteBookApplyDonation(applyId);
    }

    @GetMapping("/books")
    public List<BookResponseDto> getDonationBooks(@RequestParam BookStatusEnum bookStatus){
        return bookApplyDonationService.getDonationBooks(bookStatus);
    }

    @GetMapping
    public List<BookApplyDonationResponseDto> getBookApplyDonations(){
        return bookApplyDonationService.getBookApplyDonations();
    }


}

