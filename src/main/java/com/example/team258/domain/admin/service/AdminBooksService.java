package com.example.team258.domain.admin.service;

import com.example.team258.common.dto.BooksPageResponseDto;
import com.example.team258.common.entity.*;
import com.example.team258.domain.admin.dto.AdminBooksRequestDto;
import com.example.team258.domain.admin.dto.AdminBooksResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.domain.admin.repository.AdminBooksRepository;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
//import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.BooleanBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

//import com.querydsl.core.types.dsl.BooleanExpression;
//import static com.example.QBook.book; // QBook 클래스의 정적 임포트

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminBooksService {
    private final AdminBooksRepository adminBooksRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Transactional
    public MessageDto createBook(AdminBooksRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 카테고리 ID를 이용해서 실제 카테고리 조회
        BookCategory bookCategory = checkExistingCategory(requestDto);

        // book 생성을 위한 샘플 카테고리 생성
        bookCategoryRepository.save(bookCategory);

        // 이미 도서가 존재하는지 확인
        if (!bookAlreadyExists(requestDto)) {
            // 새로운 도서 생성
            Book newBook = new Book(requestDto, bookCategory);
            adminBooksRepository.save(newBook);
            return new MessageDto("도서 추가가 완료되었습니다.");
        } else {
            return new MessageDto("이미 존재하는 도서입니다.");
        }
    }

    public BooksPageResponseDto findBooksWithPaginationAndSearching(User loginUser, String keyword, Pageable pageable) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 있을 경우 검색 조건을 추가
        if (StringUtils.hasText(keyword))
            builder.and(qBook.bookName.containsIgnoreCase(keyword));

        // 페이징된 엔티티를 Dto로 변환하여 반환
        Page<Book> adminBooks = adminBooksRepository.findAll(builder, pageable);
        int totalPages = adminBooks.getTotalPages();
        List<AdminBooksResponseDto> booksResponseDtos = adminBooks.stream().map(AdminBooksResponseDto::new).toList();

        return new BooksPageResponseDto(booksResponseDtos, totalPages);
    }


    @Transactional
    public MessageDto updateBook(AdminBooksRequestDto requestDto, Long bookId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 ID를 이용해서 책 조회
        Book book = checkExistingBook(bookId);

        // 도서의 카테고리 ID를 이용해서 실제 카테고리 조회
        BookCategory bookCategory = checkExistingCategory(requestDto);

        book.update(requestDto, bookCategory);

        adminBooksRepository.save(book);

        return new MessageDto("도서 정보가 수정되었습니다.");
    }

    @Transactional
    public MessageDto deleteBook(Long bookId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 ID를 이용해서 책 조회
        Book book = checkExistingBook(bookId);

        adminBooksRepository.delete(book);

        return new MessageDto("도서가 삭제되었습니다.");
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

    private boolean bookAlreadyExists(AdminBooksRequestDto requestDto) {
        // 도서의 이름으로 중복 확인
        return adminBooksRepository.findByBookName(requestDto.getBookName()).isPresent();
    }

    public List<AdminBooksResponseDto> getAllBooks() {
        return adminBooksRepository.findAll().stream()
                .map(books -> new AdminBooksResponseDto(books)).toList();
    }

    public AdminBooksResponseDto getBookById(Long bookId, User loginUser) {
        // 로그인한 사용자 관리자 확인
        validateUserAuthority(loginUser);

        // 도서의 ID를 이용해서 책 조회
        Book book = checkExistingBook(bookId);

        return new AdminBooksResponseDto(book);
    }
}
