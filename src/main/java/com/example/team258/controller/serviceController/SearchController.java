package com.example.team258.controller.serviceController;

import com.example.team258.dto.BookResponseDto;
import com.example.team258.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<Page<BookResponseDto>> getAllBooks(@RequestParam("page") int page) {
        return ResponseEntity.ok(searchService.getAllBooks(page - 1));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<BookResponseDto> getBookById(@PathVariable Long bookId) {
        return ResponseEntity.ok(searchService.getBookById(bookId));
    }

//    @GetMapping("/search")
//    public Page<BookResponseDto> getAllBooksByCategory(@RequestParam("bookCategoryName") String bookCategoryName,
//                                                       @RequestParam("page") int page) {
//        return searchService.getAllBooksByCategory(bookCategoryName,page);
//    }
//
//    @GetMapping("/search")
//    public Page<BookResponseDto> getAllBooksByKeyword(@RequestParam("keyword") String keyword,
//                                                      @RequestParam("page") int page) {
//        return searchService.getAllBooksByKeyword(keyword,page);
//    }

    @GetMapping("/search")
    public ResponseEntity<Page<BookResponseDto>> getAllBooksByCategoryOrKeyword(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                                                                                @RequestParam(value = "keyword", required = false) String keyword,
                                                                                @RequestParam(value = "page") int page) {
        if(bookCategoryName==null){
            return ResponseEntity.ok(searchService.getAllBooksByKeyword(keyword,page-1));
        } else if(keyword == null){
            return ResponseEntity.ok(searchService.getAllBooksByCategory(bookCategoryName,page-1));
        }
        return ResponseEntity.ok(searchService.getAllBooksByCategoryOrKeyword(bookCategoryName,keyword,page-1));
    }
}
