package com.example.team258.domain.admin.service;

import com.example.team258.common.dto.BooksCategoryPageResponseDto;
import com.example.team258.common.entity.*;
import com.example.team258.domain.admin.dto.AdminCategoriesRequestDto;
import com.example.team258.domain.admin.dto.AdminCategoriesResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.domain.admin.repository.AdminBooksRepository;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminCategoriesService {
    private final BookCategoryRepository bookCategoryRepository;
    private final AdminBooksRepository adminBooksRepository;

    @Transactional
    public MessageDto createBookCategory(AdminCategoriesRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 새로운 카테고리 생성
        BookCategory newBookCategory = new BookCategory(requestDto);

        // 카테고리 저장
        bookCategoryRepository.save(newBookCategory);

        return new MessageDto("카테고리 추가가 완료되었습니다.");
    }

    @Transactional
    public MessageDto createSubBookCategory(Long parentId, AdminCategoriesRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 부모 카테고리 확인
        BookCategory parentCategory = bookCategoryRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("부모 카테고리를 찾을 수 없습니다."));

        // 새로운 하위 카테고리 생성
        BookCategory newSubBookCategory = new BookCategory(requestDto);
        parentCategory.addChildCategory(newSubBookCategory);

        // 카테고리 저장
        bookCategoryRepository.save(newSubBookCategory);

        return new MessageDto("하위 카테고리 추가가 완료되었습니다.");
    }

    public List<AdminCategoriesResponseDto> getAllCategories() {
        // 로그인한 사용자 관리자 확인
        //validateUserAuthority(loginUser);

        return bookCategoryRepository.findAll().stream()
                .map(AdminCategoriesResponseDto::new)
                .toList();
    }

    public BooksCategoryPageResponseDto findBooksCategoriesWithPaginationAndSearching(User loginUser, String keyword, Pageable pageable) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        QBookCategory qBookCategory = QBookCategory.bookCategory;
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있을 경우 검색 조건을 추가
        if (StringUtils.hasText(keyword))
            builder.and(qBookCategory.bookCategoryName.containsIgnoreCase(keyword));

        // 페이징된 엔티티를 Dto로 변환하여 반환
        Page<BookCategory> bookCategories = bookCategoryRepository.findAll(builder, pageable);
        int totalPages = bookCategories.getTotalPages();
        List<AdminCategoriesResponseDto> categoriesResponseDtos = bookCategories.stream().map(AdminCategoriesResponseDto::new).toList();

        return new BooksCategoryPageResponseDto(categoriesResponseDtos, totalPages);

    }

    @Transactional
    public MessageDto updateBookCategory(Long bookCategoryId, AdminCategoriesRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 카테고리 확인
        BookCategory category = checkExistingBookCategory(bookCategoryId);

        // 카테고리 이름 및 ISBN 코드 업데이트
        category.updateBookCategory(requestDto.getBookCategoryName(), requestDto.getBookCategoryIsbnCode());

        // 카테고리 저장
        bookCategoryRepository.save(category);

        return new MessageDto("카테고리가 수정되었습니다.");
    }

    /*
    * 해당 메소드는 책의 정보 업데이트와 중복 될 수 있음. 필요 없을 시 삭제 가능
    * */
    @Transactional
    public MessageDto updateBookCategory(Long bookId, Long bookCategoryId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서 확인
        Book book = checkExistingBook(bookId);

        // 카테고리 확인
        BookCategory category = checkExistingBookCategory(bookCategoryId);

        // 도서의 카테고리 업데이트
        book.updateBookCategory(category);

        return new MessageDto("도서의 카테고리가 업데이트되었습니다.");
    }

    @Transactional
    public MessageDto deleteBookCategory(Long bookCategoryId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 카테고리 확인
        BookCategory category = checkExistingBookCategory(bookCategoryId);

        // 삭제 전에 부모 카테고리에서 제거
        removeFromParentCategory(category);

        // 카테고리 삭제
        bookCategoryRepository.delete(category);

        return new MessageDto("카테고리가 삭제되었습니다.");
    }

    private void validateUserAuthority(User loginUser) {
        UserRoleEnum userRole = loginUser.getRole();
        if (userRole == null || !userRole.equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("해당 작업을 수행할 권한이 없습니다.");
        }
    }

    private Book checkExistingBook(Long bookId) {
        return adminBooksRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("도서를 찾을 수 없습니다."));
    }
    private BookCategory checkExistingBookCategory(Long bookCategoryId) {
        return bookCategoryRepository.findById(bookCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
    }
    public void updateParentCategory(BookCategory parentCategory, String newBookCategoryName, Long newBookCategoryIsbnCode) {
        if (parentCategory != null) {
            parentCategory.updateBookCategory(newBookCategoryName, newBookCategoryIsbnCode);
            bookCategoryRepository.save(parentCategory);
        }
    }

    public void removeFromParentCategory(BookCategory category) {
        BookCategory parentCategory = category.getParentCategory();
        if (parentCategory != null) {
            parentCategory.getChildCategories().remove(category);
            bookCategoryRepository.save(parentCategory);
        }
    }
}