package com.example.team258.domain.donation.controller;

import com.example.team258.common.dto.MessageDto;
import com.example.team258.domain.donation.dto.BookDonationEventRequestDto;
import com.example.team258.domain.donation.dto.BookDonationEventResponseDto;
import com.example.team258.domain.donation.dto.BookDonationSettingCancelRequestDto;
import com.example.team258.domain.donation.dto.BookDonationSettingRequestDto;
import com.example.team258.domain.donation.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/donation")
@RequiredArgsConstructor
public class BookDonationEventController {

    private final BookDonationEventService bookDonationEventService;
    @PostMapping
    public ResponseEntity<MessageDto> createDonationEvent(@RequestBody BookDonationEventRequestDto bookDonationEventRequestDto){
        return bookDonationEventService.createDonationEvent(bookDonationEventRequestDto);
    }

    @PutMapping("/{donationId}")
    public ResponseEntity<MessageDto> updateDonationEvent(@PathVariable Long donationId, @RequestBody BookDonationEventRequestDto bookDonationEventRequestDto){
        return bookDonationEventService.updateDonationEvent(donationId,bookDonationEventRequestDto);
    }

    @DeleteMapping("/{donationId}")
    public ResponseEntity<MessageDto> deleteDonationEvent(@PathVariable Long donationId){
        return bookDonationEventService.deleteDonationEvent(donationId);
    }

    @GetMapping
    public ResponseEntity<List<BookDonationEventResponseDto>> getDonationEvent(){
        return ResponseEntity.ok().body(bookDonationEventService.getDonationEvent());
    }

    @PutMapping("/setting")
    public ResponseEntity<MessageDto> settingDonationEvent(@RequestBody BookDonationSettingRequestDto bookDonationSettingRequestDto){
        return ResponseEntity.ok().body(bookDonationEventService.settingDonationEvent(bookDonationSettingRequestDto));
    }

    @PutMapping("/settingCancel")
    public ResponseEntity<MessageDto> settingCancelDonationEvent(@RequestBody BookDonationSettingCancelRequestDto bookDonationSettingCancelRequestDto){
        return ResponseEntity.ok().body(bookDonationEventService.settingCancelDonationEvent(bookDonationSettingCancelRequestDto));
    }

    @DeleteMapping("/end/{donationId}")
    public ResponseEntity<MessageDto> endDonationEvent(@PathVariable Long donationId){
        return ResponseEntity.ok().body(bookDonationEventService.endDonationEvent(donationId));
    }

}
