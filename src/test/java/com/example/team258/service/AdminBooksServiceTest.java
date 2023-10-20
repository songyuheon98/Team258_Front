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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
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
                    .bookAuthor("Test Author")
                    .bookPublish("2011")
                    .bookCategoryId(1L)
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
            when(adminBooksRepository.save(any(Book.class))).thenReturn(new Book());

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
                    .bookAuthor("Test Author")
                    .bookPublish("2011")
                    .bookCategoryId(1L)
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
        @Test
        @DisplayName("GET - 도서 전체 목록 조회 성공 (검색어 미사용)")
        void 도서_전체_목록_조회_테스트_검색어_미사용() {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            List<Book> books = List.of(
                    Book.builder()
                            .bookId(1L)
                            .bookName("Book1")
                            .bookAuthor("Author1")
                            .bookPublish("2011")
                            .bookCategory(bookCategory)
                            .build(),
                    Book.builder()
                            .bookId(2L)
                            .bookName("Book2")
                            .bookAuthor("Author2")
                            .bookPublish("2011")
                            .bookCategory(bookCategory)
                            .build()
            );

            // 모의 객체 설정
            when(adminBooksRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(books, PageRequest.of(0, 10), books.size()));

            // when
            Page<AdminBooksResponseDto> responseDtoPage = adminBooksService.getAllBooksPagedAndSearched(adminUser, null, PageRequest.of(0, 10));

            // then
            // 서비스가 기대한 대로 작동하는지 검증
            verify(adminBooksRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

            // 응답이 기대한 대로 구성되어 있는지 확인
            assertAll(
                    () -> assertNotNull(responseDtoPage),
                    () -> assertEquals(2, responseDtoPage.getContent().size()), // 예제로 2개의 도서를 추가하였으므로
                    () -> assertEquals("Book1", responseDtoPage.getContent().get(0).getBookName()),
                    () -> assertEquals("Book2", responseDtoPage.getContent().get(1).getBookName())
                    // 나머지 필드들도 확인해보기
            );
        }

        @Test
        @DisplayName("GET - 도서 전체 목록 조회 성공 (검색어 사용)")
        void 도서_전체_목록_조회_테스트_검색어_사용() {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            List<Book> books = List.of(
                    Book.builder()
                            .bookId(1L)
                            .bookName("Book1")
                            .bookAuthor("Author1")
                            .bookPublish("2011")
                            .bookCategory(bookCategory)
                            .build(),
                    Book.builder()
                            .bookId(2L)
                            .bookName("Novel1")
                            .bookAuthor("Author2")
                            .bookPublish("2011")
                            .bookCategory(bookCategory)
                            .build()
            );

            // 검색어 설정
            String keyword = "Book1";

            // 모의 객체 설정
            when(adminBooksRepository.findAll(any(Specification.class), any(Pageable.class)))
                    .thenReturn(new PageImpl<>(books, PageRequest.of(0, 10), books.size()));

            // when
            Page<AdminBooksResponseDto> responseDtoPage = adminBooksService.getAllBooksPagedAndSearched(adminUser, keyword, PageRequest.of(0, 10));

            // then
            // 서비스가 기대한 대로 작동하는지 검증
            verify(adminBooksRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));

            // 응답이 기대한 대로 구성되어 있는지 확인
            assertAll(
                    () -> assertNotNull(responseDtoPage),
                    () -> assertEquals("Book1", responseDtoPage.getContent().get(0).getBookName())
                    // 나머지 필드들도 확인해보기
            );

        }



        @Test
        @DisplayName("GET - 도서 선택 조회 성공")
        void 도서_조회_테스트() {
            // given
            Long bookId = 1L;
            User adminUser = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            Book existingBook = Book.builder()
                    .bookId(bookId)
                    .bookName("Existing Book")
                    .bookAuthor("Existing Author")
                    .bookPublish("2011")
                    .bookCategory(bookCategory)
                    .build();

            // 모의 객체 설정
            when(adminBooksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));

            // when
            AdminBooksResponseDto responseDto = adminBooksService.getBookById(bookId, adminUser);

            // then
            // 서비스가 기대한 대로 작동하는지 검증
            verify(adminBooksRepository, times(1)).findById(bookId);

            // 응답이 기대한 대로 구성되어 있는지 확인
            assertAll(
                    () -> assertNotNull(responseDto),
                    () -> assertEquals(existingBook.getBookName(), responseDto.getBookName()),
                    () -> assertEquals(existingBook.getBookAuthor(), responseDto.getBookAuthor()),
                    () -> assertEquals(existingBook.getBookPublish(), responseDto.getBookPublish())
                    //() -> assertEquals(existingBook.getBookCategory().getBookCategoryId(), responseDto.getBookCategory().getBookCategoryId())
            );
        }


        @Test
        @DisplayName("UPDATE - 도서 수정 성공")
        void 도서_업데이트_테스트() {
            // given
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Updated Book")
                    .bookAuthor("Updated Author")
                    .bookPublish("2011")
                    .bookCategoryId(1L)
                    .build();

            Long bookId = 1L;

            User adminUser = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            Book existingBook = Book.builder()
                    .bookId(bookId)
                    .bookName("Existing Book")
                    .bookAuthor("Existing Author")
                    .bookPublish("2011")
                    .bookCategory(bookCategory)
                    .build();

            // 모의 객체 설정
            when(adminBooksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));

            // when
            ResponseEntity<MessageDto> responseEntity = adminBooksService.updateBook(requestDto, bookId, adminUser);

            // then
            // 서비스가 기대한 대로 작동하는지 검증
            verify(adminBooksRepository, times(1)).findById(bookId);
            verify(bookCategoryRepository, times(1)).findById(1L);
            //verify(adminBooksRepository, times(1)).save(any());

            // 응답이 기대한 대로 구성되어 있는지 확인
            assertAll(
                    () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                    () -> assertNotNull(responseEntity.getBody()),
                    () -> assertEquals("도서 정보가 수정되었습니다.", responseEntity.getBody().getMsg())
            );
        }
        @Test
        @DisplayName("DELETE - 도서 삭제 성공")
        void 도서_삭제_테스트() {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            Book existingBook = Book.builder()
                    .bookId(1L)
                    .build();

            when(adminBooksRepository.findById(1L)).thenReturn(Optional.of(existingBook));

            // when
            ResponseEntity<MessageDto> responseEntity = adminBooksService.deleteBook(1L, adminUser);

            // then
            verify(adminBooksRepository, times(1)).findById(1L);
            verify(adminBooksRepository, times(1)).delete(existingBook);

            // Check the response
            assertAll(
                    () -> assertEquals(HttpStatus.OK, responseEntity.getStatusCode()),
                    () -> assertNotNull(responseEntity.getBody()),
                    () -> assertEquals("도서가 삭제되었습니다.", responseEntity.getBody().getMsg())
            );
        }
    }
}