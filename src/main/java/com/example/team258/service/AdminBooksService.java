package com.example.team258.service;

import com.example.team258.dto.AdminBooksRequestDto;
import com.example.team258.dto.AdminBooksResponseDto;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBooksService {
    private final AdminBooksRepository adminBooksRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Transactional
    public ResponseEntity<MessageDto> createBook(AdminBooksRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 카테고리 ID를 이용해서 실제 카테고리 조회
        BookCategory bookCategory = checkExistingCategory(requestDto);
        /**
         * 카테고리 생성되면 테스트
         */
//        // 도서의 카테고리 ID를 이용해서 실제 카테고리 조회
//        BookCategory bookCategory = bookCategoryRepository.findById(requestDto.getCategoryId())
//                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
//
        /**
         * book 생성을 위한 샘플 카테고리 생성
         */
        bookCategoryRepository.save(bookCategory);

        // 새로운 도서 생성
        Book newBook = new Book(requestDto, bookCategory);

        adminBooksRepository.save(newBook);

        return new ResponseEntity<>(new MessageDto("도서 추가가 완료되었습니다."), null, HttpStatus.OK);
    }

    public List<AdminBooksResponseDto> getAllBooks(User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        return adminBooksRepository.findAll().stream()
                .map(AdminBooksResponseDto::new)
                .toList();
    }

    public AdminBooksResponseDto getBookById(Long bookId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 ID를 이용해서 책 조회
        Book book = checkExistingBook(bookId);

        return new AdminBooksResponseDto(book);
    }

    @Transactional
    public ResponseEntity<MessageDto> updateBook(AdminBooksRequestDto requestDto, Long bookId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 ID를 이용해서 책 조회
        Book book = checkExistingBook(bookId);

        // 도서의 카테고리 ID를 이용해서 실제 카테고리 조회
        BookCategory bookCategory = checkExistingCategory(requestDto);

        book.update(requestDto, bookCategory);

        return new ResponseEntity<>(new MessageDto("도서 정보가 수정되었습니다."), null, HttpStatus.OK);
    }

    public ResponseEntity<MessageDto> deleteBook(Long bookId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 ID를 이용해서 책 조회
        Book book = checkExistingBook(bookId);

        adminBooksRepository.delete(book);

        return new ResponseEntity<>(new MessageDto("도서가 삭제되었습니다."), null, HttpStatus.OK);
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

    private BookCategory checkExistingCategory(AdminBooksRequestDto requestDto) {
        return bookCategoryRepository.findById(requestDto.getBookCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));
    }
}
