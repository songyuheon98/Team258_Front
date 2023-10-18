package com.example.team258.controller.viewController.admin;

import com.example.team258.service.AdminCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BooksViewController {
    private final AdminCategoriesService adminCategoriesService;

    @GetMapping("/admin/books")
    public String adminBooksView(Model model) {
        // 카테고리 정보를 DB에서 가져와서 모델에 추가
        model.addAttribute("categories", adminCategoriesService.getAllCategories());

        return "admin/books";
    }
}