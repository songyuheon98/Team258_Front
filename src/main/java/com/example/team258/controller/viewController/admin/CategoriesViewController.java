package com.example.team258.controller.viewController.admin;

import com.example.team258.dto.AdminCategoriesResponseDto;
import com.example.team258.entity.BookCategory;
import com.example.team258.repository.BookCategoryRepository;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class CategoriesViewController {
    private final BookCategoryRepository bookCategoryRepository;
    private final AdminCategoriesService adminCategoriesService;

    @GetMapping("/admin/categories")
    public String categories(
            Model model,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "keyword", required = false) String keyword,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Page<AdminCategoriesResponseDto> categoryResponsePage = adminCategoriesService.getAllCategoriesPagedAndSearch(userDetails.getUser(), keyword, PageRequest.of(page, 10));
        List<AdminCategoriesResponseDto> categoryResponseDtos = categoryResponsePage.getContent();

        model.addAttribute("categories", categoryResponseDtos);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryResponsePage.getTotalPages());

        return "/admin/categories";
    }
}
