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
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class BookApplyDonationController {

    private final BookApplyDonationService bookApplyDonationService;
    @PostMapping("/bookApplyDonation")
    public ResponseEntity<MessageDto> createBookApplyDonation(@RequestBody BookApplyDonationRequestDto bookApplyDonationRequestDto){
        return bookApplyDonationService.createBookApplyDonation(bookApplyDonationRequestDto);
    }

    @DeleteMapping("/bookApplyDonation/{applyId}")
    public ResponseEntity<MessageDto> deleteBookApplyDonation(@PathVariable Long applyId){
        return bookApplyDonationService.deleteBookApplyDonation(applyId);
    }

    /**
     * 책 기부 신청 목록 조회
     * @param bookStatus
     * @return
     */
    @GetMapping("/bookApplyDonation/books")
    public ResponseEntity<List<BookResponseDto>> getDonationBooks(@RequestParam BookStatusEnum bookStatus){
        return ResponseEntity.ok().body(bookApplyDonationService.getDonationBooks(bookStatus));
    }

    @GetMapping("/bookApplyDonation")
    public ResponseEntity<List<BookApplyDonationResponseDto>> getBookApplyDonations(){
        return ResponseEntity.ok().body(bookApplyDonationService.getBookApplyDonations());
    }

}

