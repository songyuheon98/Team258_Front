package com.example.team258.controller.serviceController;

import com.example.team258.dto.MessageDto;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.BookRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRentController {

    private final BookRentService bookRentService;

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
