package com.example.team258.controller.viewController.admin;

import com.example.team258.dto.AdminBooksResponseDto;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminBooksService;
import com.example.team258.service.AdminCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BooksViewController {
    private final AdminCategoriesService adminCategoriesService;
    private final AdminBooksService adminBooksService;

    @GetMapping("/admin/books")
    public String adminBooksView(Model model) {
        // 카테고리 정보를 DB에서 가져와서 모델에 추가
        model.addAttribute("categories", adminCategoriesService.getAllCategories());

        return "admin/books";
    }

    @GetMapping("/admin/booksManage")
    public String adminBooksManageView(
            Model model,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PageableDefault(size = 10, sort = "bookId", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(value = "keyword", required = false) String keyword) {

        Page<AdminBooksResponseDto> bookResponsePage = adminBooksService.getAllBooks(userDetails.getUser(), keyword, pageable);
        List<AdminBooksResponseDto> bookResponseDtos = bookResponsePage.getContent();

        model.addAttribute("books", bookResponseDtos);
        model.addAttribute("currentPage", bookResponsePage.getNumber());
        model.addAttribute("totalPages", bookResponsePage.getTotalPages());

        return "/admin/booksManage";
    }
}