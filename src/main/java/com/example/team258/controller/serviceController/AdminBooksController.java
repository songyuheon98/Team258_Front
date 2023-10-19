package com.example.team258.controller.serviceController;

import com.example.team258.dto.AdminBooksRequestDto;
import com.example.team258.dto.AdminBooksResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminBooksService;
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
        return adminBooksService.createBook(requestDto, userDetails.getUser());
    }

    //// READ ALL
    //@GetMapping
    //public ResponseEntity<List<AdminBooksResponseDto>> getAllBooks(@AuthenticationPrincipal UserDetailsImpl userDetails){
    //    return ResponseEntity.ok(adminBooksService.getAllBooks(userDetails.getUser()));
    //}
    // READ ALL with Paging
    @GetMapping
    public ResponseEntity<Page<AdminBooksResponseDto>> getAllBooksPaged(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "bookId", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<AdminBooksResponseDto> bookResponsePage = adminBooksService.getAllBooks(userDetails.getUser(), pageable);
        return ResponseEntity.ok(bookResponsePage);
    }

    // READ SELECT
    @GetMapping("/{bookId}")
    public ResponseEntity<AdminBooksResponseDto> getBook(@PathVariable Long bookId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(adminBooksService.getBookById(bookId, userDetails.getUser()));
    }

    // UPDATE SELECT
    @PutMapping("/{bookId}")
    public ResponseEntity<MessageDto> updateBook(@RequestBody AdminBooksRequestDto requestDto,
                                                 @PathVariable Long bookId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminBooksService.updateBook(requestDto, bookId, userDetails.getUser());
    }

    // DELETE SELECT
    @DeleteMapping("/{bookId}")
    public ResponseEntity<MessageDto> deleteBook(@PathVariable Long bookId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminBooksService.deleteBook(bookId, userDetails.getUser());
    }
}


