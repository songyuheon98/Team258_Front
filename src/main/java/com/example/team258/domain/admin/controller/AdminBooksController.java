package com.example.team258.domain.admin.controller;

import com.example.team258.domain.admin.dto.AdminBooksRequestDto;
import com.example.team258.domain.admin.dto.AdminBooksResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.admin.service.AdminBooksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin/books")
@RequiredArgsConstructor
public class AdminBooksController {
    private final AdminBooksService adminBooksService;

    // CREATE
    @PostMapping
    public ResponseEntity<MessageDto> createBook(@RequestBody AdminBooksRequestDto requestDto,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminBooksService.createBook(requestDto, userDetails.getUser()));
    }

    // READ ALL with Paging and Search
    @GetMapping
    public ResponseEntity<List<AdminBooksResponseDto>> getAllBooks() {
        return ResponseEntity.ok().body(adminBooksService.getAllBooks());
    }

    // READ SELECT
    @GetMapping("/{bookId}")
    public ResponseEntity<AdminBooksResponseDto> getBook(@PathVariable Long bookId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminBooksService.getBookById(bookId, userDetails.getUser()));
    }

    // UPDATE SELECT
    @PutMapping("/{bookId}")
    public ResponseEntity<MessageDto> updateBook(@RequestBody AdminBooksRequestDto requestDto,
                                                 @PathVariable Long bookId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminBooksService.updateBook(requestDto, bookId, userDetails.getUser()));
    }

    // DELETE SELECT
    @DeleteMapping("/{bookId}")
    public ResponseEntity<MessageDto> deleteBook(@PathVariable Long bookId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminBooksService.deleteBook(bookId, userDetails.getUser()));
    }
}


