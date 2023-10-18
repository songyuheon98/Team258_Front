package com.example.team258.controller.viewController.admin;

import com.example.team258.dto.AdminBooksResponseDto;
import com.example.team258.dto.BookResponseDto;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminBooksService;
import com.example.team258.service.AdminCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String adminBooksManageView(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<AdminBooksResponseDto> bookResponseDtos = adminBooksService.getAllBooks(userDetails.getUser());
        model.addAttribute("books", bookResponseDtos);
        return "/admin/booksManage";
    }
}