package com.example.team258.controller.serviceController;

import com.example.team258.dto.BookDonationEventRequestDto;
import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.service.BookDonationEventService;
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

}
