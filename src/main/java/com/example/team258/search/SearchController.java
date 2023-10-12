package com.example.team258.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public Page<BookResponseDto> getAllBooks(@RequestParam("page") int page) {
        return searchService.getAllBooks(page - 1);
    }

    @GetMapping("/{bookId}")
    public BookResponseDto getBookById(@PathVariable Long bookId) {
        return searchService.getBookById(bookId);
    }



}
