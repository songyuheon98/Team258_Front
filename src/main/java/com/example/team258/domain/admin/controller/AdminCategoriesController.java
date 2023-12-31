package com.example.team258.domain.admin.controller;

import com.example.team258.domain.admin.dto.AdminCategoriesRequestDto;
import com.example.team258.domain.admin.dto.AdminCategoriesResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.admin.service.AdminCategoriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoriesController {
    private final AdminCategoriesService adminCategoriesService;

    // CREATE
    @PostMapping
    public ResponseEntity<MessageDto> createCategory(@RequestBody AdminCategoriesRequestDto requestDto,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminCategoriesService.createBookCategory(requestDto, userDetails.getUser()));
    }

    // CREATE SubCategory
    @PostMapping("/{parentId}/subcategories")
    public ResponseEntity<MessageDto> createSubBookCategory(@PathVariable Long parentId,
                                                        @RequestBody AdminCategoriesRequestDto requestDto,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminCategoriesService.createSubBookCategory(parentId, requestDto, userDetails.getUser()));
    }

    // READ All Categories with Paging and Search
    @GetMapping
    public ResponseEntity<List<AdminCategoriesResponseDto>> getAllCategories() {
        return ResponseEntity.ok().body(adminCategoriesService.getAllCategories());
    }

    // UPDATE Category Name
    @PutMapping("/{bookCategoryId}")
    public ResponseEntity<MessageDto> updateBookCategoryName(@PathVariable Long bookCategoryId,
                                                             @RequestBody AdminCategoriesRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminCategoriesService.updateBookCategory(bookCategoryId, requestDto, userDetails.getUser()));
    }

    // UPDATE Book's Category
    // 이 컨트롤러는 도서 정보 수정으로 옮겨야 할 수도 있음
    @PutMapping("/books/{bookId}/categories/{categoryId}")
    public ResponseEntity<MessageDto> updateBookCategory(@PathVariable Long bookId,
                                                         @PathVariable Long categoryId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminCategoriesService.updateBookCategory(bookId, categoryId, userDetails.getUser()));
    }

    // DELETE Category
    @DeleteMapping("/{bookCategoryId}")
    public ResponseEntity<MessageDto> deleteBookCategory(@PathVariable Long bookCategoryId,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok().body(adminCategoriesService.deleteBookCategory(bookCategoryId, userDetails.getUser()));
    }
}