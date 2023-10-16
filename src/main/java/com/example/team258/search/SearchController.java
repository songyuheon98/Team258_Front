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
    public Page<BookResponseDto> getAllBooksByCategoryOrKeyword(@RequestParam("bookCategoryName") String bookCategoryName,
                                                                @RequestParam("keyword") String keyword,
                                                                @RequestParam("page") int page) {
        if(keyword==null){
            return searchService.getAllBooksByKeyword(keyword,page);
        } else if(bookCategoryName == null){
            return searchService.getAllBooksByCategory(keyword,page);
        }
        return searchService.getAllBooksByCategoryOrKeyword(bookCategoryName,keyword,page);
    }
}
