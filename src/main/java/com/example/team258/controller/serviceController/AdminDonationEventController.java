package com.example.team258.controller.serviceController;

import com.example.team258.dto.BookDonationEventRequestDto;
import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.service.AdminDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/donation")
@RequiredArgsConstructor
public class AdminDonationEventController {

    private final AdminDonationEventService adminDonationEventService;
    @PostMapping
    public ResponseEntity<MessageDto> createDonationEvent(@RequestBody BookDonationEventRequestDto bookDonationEventRequestDto){
        return adminDonationEventService.createDonationEvent(bookDonationEventRequestDto);
    }

    @PutMapping("/{donationId}")
    public ResponseEntity<MessageDto> updateDonationEvent(@PathVariable Long donationId, @RequestBody BookDonationEventRequestDto bookDonationEventRequestDto){
        return adminDonationEventService.updateDonationEvent(donationId,bookDonationEventRequestDto);
    }

    @DeleteMapping("/{donationId}")
    public ResponseEntity<MessageDto> deleteDonationEvent(@PathVariable Long donationId){
        return adminDonationEventService.deleteDonationEvent(donationId);
    }

    @GetMapping
    public List<BookDonationEventResponseDto> getDonationEvent(){
        return adminDonationEventService.getDonationEvent();
    }

}
