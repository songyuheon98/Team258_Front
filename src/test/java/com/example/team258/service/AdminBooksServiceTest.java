package com.example.team258.service;

import com.example.team258.dto.AdminBooksRequestDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.AdminBooksRepository;
import com.example.team258.repository.BookCategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminBooksServiceTest {

    @Mock
    private AdminBooksRepository adminBooksRepository;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @InjectMocks
    private AdminBooksService adminBooksService;

    @Nested
    @DisplayName("AdminBooksService - CRUD 테스트")
    class BasicReadTest {
        @Test
        @DisplayName("CREATE - 도서 추가 성공")
        void 도서_추가_테스트() {
            // given
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Test Book")
                    .bookInfo("Test Book Info")
                    .bookAuthor("Test Author")
                    .bookPublish(LocalDateTime.now())
                    .categoryId(1L)
                    .build();

            User adminUser = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));
            when(adminBooksRepository.save(any())).thenReturn(new Book());

            // when
            ResponseEntity<MessageDto> responseEntity = adminBooksService.createBook(requestDto, adminUser);

            // then
            verify(bookCategoryRepository, times(1)).findById(1L);
            verify(adminBooksRepository, times(1)).save(any());

            // Check the response
            assertAll(
                    () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                    () -> assertNotNull(responseEntity.getBody()),
                    () -> assertEquals("도서 추가가 완료되었습니다.", responseEntity.getBody().getMsg())
            );
        }
        @Test
        @DisplayName("CREATE - 도서 추가 실패 - 권한 없음")
        void 일반_유저로_도서_추가_시도_테스트() {
            // given
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Test Book")
                    .bookInfo("Test Book Info")
                    .bookAuthor("Test Author")
                    .bookPublish(LocalDateTime.now())
                    .categoryId(1L)
                    .build();

            User user = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.USER) // ADMIN이 아닌 일반 USER
                    .build();

            // when & then
            assertThrows(IllegalArgumentException.class,
                    () -> adminBooksService.createBook(requestDto, user),
                    "도서를 추가할 권한이 없는 USER가 도서를 추가하려고 할 때 IllegalArgumentException이 발생해야 합니다.");
        }
    }
}