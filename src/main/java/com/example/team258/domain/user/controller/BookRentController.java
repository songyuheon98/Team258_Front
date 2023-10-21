package com.example.team258.domain.user.controller;

import com.example.team258.domain.user.dto.BookRentResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.user.service.BookRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRentController {

    private final BookRentService bookRentService;

    @GetMapping("rental")
    public ResponseEntity<List<BookRentResponseDto>> getRental(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookRentService.getRental(userDetails.getUser()));
    }

    @PostMapping("/{bookId}/rental")
    public ResponseEntity<MessageDto> createRental(@PathVariable Long bookId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookRentService.createRental(bookId, userDetails.getUser()));
    }

    @DeleteMapping("/{bookId}/rental")
    public ResponseEntity<MessageDto> deleteRental(@PathVariable Long bookId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(bookRentService.deleteRental(bookId, userDetails.getUser()));
    }
}
