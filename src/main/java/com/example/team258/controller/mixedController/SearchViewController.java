package com.example.team258.controller.mixedController;

import com.example.team258.service.AdminCategoriesService;
import com.example.team258.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SearchViewController {
    private final SearchService searchService;
    private final AdminCategoriesService adminCategoriesService;

    @GetMapping("/search")
    public String mySearchView(@RequestParam(value = "bookCategoryName", required = false) String bookCategoryName,
                               @RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "page", required = false) Integer page,
                               Model model) {
        model.addAttribute("categories", adminCategoriesService.getAllCategories());
        model.addAttribute("bookMaxCount",searchService.getMaxCount());
        if (page == null) page =1;
        if(bookCategoryName == null && keyword == null)
            model.addAttribute("books",searchService.getAllBooks(page - 1));
        else if(bookCategoryName == null && keyword != null)
            model.addAttribute("books",searchService.getAllBooksByKeyword(keyword,page-1));
        else if(bookCategoryName != null && keyword == null)
            model.addAttribute("books",searchService.getAllBooksByCategory(bookCategoryName,page-1));
        else if(bookCategoryName != null && keyword != null)
            model.addAttribute("books",searchService.getAllBooksByCategoryOrKeyword(bookCategoryName,keyword,page-1));
        return "search";
    }


}

//    @GetMapping("/admin/users")
//    public String adminView(Model model) {
//        List<User> users = userRepository.findAll();
//        List<UserResponseDto> userResponseDtos = users.stream().
//                map(UserResponseDto::new).collect(Collectors.toList());
//
//        model.addAttribute("users", userResponseDtos);
//
//        return "admin";
//    }