package com.example.team258.controller.viewController.admin;

import com.example.team258.dto.AdminCategoriesResponseDto;
import com.example.team258.entity.BookCategory;
import com.example.team258.repository.BookCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesViewController {
    private final BookCategoryRepository bookCategoryRepository;


    @GetMapping
    public String categories(Model model) {
        List<BookCategory> bookCategories = bookCategoryRepository.findAll();
        List<AdminCategoriesResponseDto> adminCategoriesResponseDtos = bookCategories.stream().
                map(AdminCategoriesResponseDto::new).collect(Collectors.toList());
        model.addAttribute("categories", adminCategoriesResponseDtos);
        return "admin/categories";
    }
}
