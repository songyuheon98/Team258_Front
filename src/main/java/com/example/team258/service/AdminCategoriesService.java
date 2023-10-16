package com.example.team258.service;

import com.example.team258.dto.AdminCategoriesRequestDto;
import com.example.team258.dto.AdminCategoriesResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.AdminBooksRepository;
import com.example.team258.repository.BookCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCategoriesService {
    private final BookCategoryRepository bookCategoryRepository;
    private final AdminBooksRepository adminBooksRepository;

    @Transactional
    public ResponseEntity<MessageDto> createBookCategory(AdminCategoriesRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 새로운 카테고리 생성
        BookCategory newBookCategory = new BookCategory(requestDto);

        // 이미 존재하는 카테고리인지 확인
        BookCategory existingBookCategory = bookCategoryRepository.findByBookCategoryName(requestDto.getBookCategoryName());
        if (existingBookCategory != null) {
            // 이미 존재하는 경우, 존재하는 카테고리를 선택
            newBookCategory = existingBookCategory;
        }

        // 카테고리 저장
        bookCategoryRepository.save(newBookCategory);

        return new ResponseEntity<>(new MessageDto("카테고리 추가가 완료되었습니다."), null, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<MessageDto> createSubBookCategory(Long parentId, AdminCategoriesRequestDto requestDto, User loginUser) {
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

        return new ResponseEntity<>(new MessageDto("하위 카테고리 추가가 완료되었습니다."), null, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public List<AdminCategoriesResponseDto> getAllCategories(User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        return bookCategoryRepository.findAll().stream()
                .map(AdminCategoriesResponseDto::new)
                .toList();
    }

    @Transactional
    public ResponseEntity<MessageDto> updateBookCategoryName(Long bookCategoryId, AdminCategoriesRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 카테고리 확인
        BookCategory category = checkExistingBookCategory(bookCategoryId);

        // 부모 카테고리에서도 업데이트
        updateParentCategoryName(category.getParentCategory(), requestDto.getBookCategoryName());

        // 카테고리 이름 업데이트
        category.changeBookCategoryName(requestDto.getBookCategoryName());

        // 카테고리 저장
        bookCategoryRepository.save(category);

        return new ResponseEntity<>(new MessageDto("카테고리 이름이 수정되었습니다."), null, HttpStatus.OK);
    }

    /*
    * 해당 메소드는 책의 정보 업데이트와 중복 될 수 있음. 필요 없을 시 삭제 가능
    * */
    @Transactional
    public ResponseEntity<MessageDto> updateBookCategory(Long bookId, Long bookCategoryId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서 확인
        Book book = checkExistingBook(bookId);

        // 카테고리 확인
        BookCategory category = checkExistingBookCategory(bookCategoryId);

        // 도서의 카테고리 업데이트
        book.updateBookCategory(category);

        return new ResponseEntity<>(new MessageDto("도서의 카테고리가 업데이트되었습니다."), null, HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<MessageDto> deleteBookCategory(Long bookCategoryId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 카테고리 확인
        BookCategory category = checkExistingBookCategory(bookCategoryId);

        // 삭제 전에 부모 카테고리에서 제거
        removeFromParentCategory(category);

        // 카테고리 삭제
        bookCategoryRepository.delete(category);

        return new ResponseEntity<>(new MessageDto("카테고리가 삭제되었습니다."), null, HttpStatus.OK);
    }

    private void removeFromParentCategory(BookCategory category) {
        BookCategory parentCategory = category.getParentCategory();
        if (parentCategory != null) {
            parentCategory.getChildCategories().remove(category);
            bookCategoryRepository.save(parentCategory);
        }
    }

    private void validateUserAuthority(User loginUser) {
        if (!loginUser.getRole().equals(UserRoleEnum.ADMIN)){
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

    private void updateParentCategoryName(BookCategory parentCategory, String newBookCategoryName) {
        if (parentCategory != null) {
            parentCategory.changeBookCategoryName(newBookCategoryName);
            bookCategoryRepository.save(parentCategory);
        }
    }
}