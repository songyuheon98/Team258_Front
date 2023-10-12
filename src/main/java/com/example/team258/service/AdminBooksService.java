package com.example.team258.service;

import com.example.team258.dto.AdminBooksRequestDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.AdminBooksRepository;
import com.example.team258.repository.BookCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminBooksService {
    private final AdminBooksRepository adminBooksRepository;
    private final BookCategoryRepository bookCategoryRepository;

    @Transactional
    public ResponseEntity<MessageDto> createBook(AdminBooksRequestDto requestDto, User loginUser) {
        // 로그인한 사용자 관리자 확인
        if (!loginUser.getRole().equals(UserRoleEnum.ADMIN)){
            throw new IllegalArgumentException("도서를 추가할 권한이 없습니다.");
        }

        // 도서의 카테고리 ID를 이용해서 실제 카테고리 조회
        BookCategory bookCategory = bookCategoryRepository.findById(requestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리를 찾을 수 없습니다."));

        // 새로운 도서 생성
        Book newBook = new Book(requestDto, bookCategory);
        adminBooksRepository.save(newBook);

        return new ResponseEntity<>(new MessageDto("도서 추가가 완료되었습니다."), null, HttpStatus.OK);
    }
}
