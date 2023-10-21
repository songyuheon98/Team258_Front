package com.example.team258.domain.user.controller;

import com.example.team258.domain.user.dto.BookReservationResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.user.service.BookReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookReservationController {

    private final BookReservationService bookReservationService;

    @GetMapping("/reservation")
    public ResponseEntity<List<BookReservationResponseDto>> getRental(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookReservationService.getRental(userDetails.getUser()));
    }

    @PostMapping("/{bookId}/reservation")
    public ResponseEntity<MessageDto> createReservation(@PathVariable Long bookId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookReservationService.createReservation(bookId, userDetails.getUser()));
    }

    @DeleteMapping("/{bookId}/reservation")
    public ResponseEntity<MessageDto> deleteReservation(@PathVariable Long bookId,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookReservationService.deleteReservation(bookId, userDetails.getUser()));
    }
}
