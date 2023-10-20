package com.example.team258.controller.mixedController;

import com.example.team258.dto.BookResponseDto;
import com.example.team258.service.AdminCategoriesService;
import com.example.team258.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SearchMixedController {
    private final SearchService searchService;
    private final AdminCategoriesService adminCategoriesService;

    @GetMapping("/search")
    public String mySearchView(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page", required = false) Integer page,
                               Model model) {
        model.addAttribute("categories", adminCategoriesService.getAllCategories());

        if (page == null) page = 1;
        if (bookCategoryName == null && keyword == null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooks(page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        } else if (bookCategoryName == null && keyword != null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByKeyword(keyword, page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        } else if (bookCategoryName != null && keyword == null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByCategory(bookCategoryName, page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        } else if (bookCategoryName != null && keyword != null) {
            Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByCategoryOrKeyword(bookCategoryName, keyword, page - 1);
            model.addAttribute("books", bookResponseDtoPage.getContent());
            model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());
        }
        return "search";
    }

    @GetMapping("/search2")
    public String mySearchView2(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page", defaultValue = "1", required = false) Integer page,
                               Model model) {
        model.addAttribute("categories", adminCategoriesService.getAllCategories());

        long startTime = System.currentTimeMillis();//실행시간 측정

        Page<BookResponseDto> bookResponseDtoPage = searchService.getAllBooksByCategoryOrKeyword2(bookCategoryName, keyword, page - 1);
        model.addAttribute("books", bookResponseDtoPage.getContent());
        model.addAttribute("bookMaxCount", bookResponseDtoPage.getTotalPages());

        long endTime = System.currentTimeMillis();
        long durationTimeSec = endTime - startTime;
        System.out.println(durationTimeSec + "m/s"); // 실행시간 측정

        return "search";
    }
}