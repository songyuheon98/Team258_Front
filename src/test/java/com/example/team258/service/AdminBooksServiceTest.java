package com.example.team258.service;

import com.example.team258.common.dto.BooksPageResponseDto;
import com.example.team258.common.entity.*;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.admin.dto.AdminBooksRequestDto;
import com.example.team258.domain.admin.dto.AdminBooksResponseDto;
import com.example.team258.domain.admin.service.AdminBooksService;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.domain.admin.repository.AdminBooksRepository;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class AdminBooksServiceTest {

    @Mock
    private AdminBooksRepository adminBooksRepository;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private JPAQueryFactory queryFactory;

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
        @DisplayName("GET - 도서 전체 목록 조회 성공")
        void 도서_전체_목록_조회_테스트() {
            // given
            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            List<Book> mockBooks = List.of(
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
            when(adminBooksRepository.findAll()).thenReturn(mockBooks);

            // when
            List<AdminBooksResponseDto> responseDtoList = adminBooksService.getAllBooks();

            // then
            // 서비스가 기대한 대로 작동하는지 검증
            assertEquals(2, responseDtoList.size());
            assertEquals("Book1", responseDtoList.get(0).getBookName());
            assertEquals("Book2", responseDtoList.get(1).getBookName());
            // 나머지 필드들도 확인해보기
        }


        @Test
        @DisplayName("GET - 도서 페이징 및 검색 성공")
        void 도서_페이징_및_검색_테스트() {
            // given
            User loginUser = User.builder()
                    .userId(1L)
                    .username("user1")
                    .password("pass1")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            String keyword = "Book1";
            Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.asc("bookId")));

            QBook qBook = QBook.book;
            BooleanBuilder builder = new BooleanBuilder();

            // 검색어가 있을 경우 검색 조건을 추가
            if (StringUtils.hasText(keyword)) {
                BooleanExpression expression = qBook.bookName.containsIgnoreCase(keyword);
                builder.and(expression);
            }

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .bookCategoryName("Fiction")
                    .bookCategoryIsbnCode(100L)
                    .build();

            List<Book> mockBooks = List.of(
                    Book.builder().bookId(1L).bookName("Book1").bookAuthor("Author1").bookPublish("2021").bookCategory(bookCategory).build(),
                    Book.builder().bookId(2L).bookName("Book2").bookAuthor("Author2").bookPublish("2021").bookCategory(bookCategory).build()
            );

            Page<Book> mockPage = new PageImpl<>(mockBooks, pageable, mockBooks.size());

            // 모의 객체 설정
            when(adminBooksRepository.findAll(builder, pageable)).thenReturn(mockPage);

            // when
            BooksPageResponseDto responseDto = adminBooksService.findBooksWithPaginationAndSearching(loginUser, keyword, pageable);

            // then
            // 서비스가 기대한 대로 작동하는지 검증
            verify(adminBooksRepository, times(1)).findAll(builder, pageable);

            // 응답이 기대한 대로 구성되어 있는지 확인
            assertEquals(2, responseDto.getAdminBooksResponseDtos().size());
            assertEquals("Book1", responseDto.getAdminBooksResponseDtos().get(0).getBookName());
            assertEquals("Book2", responseDto.getAdminBooksResponseDtos().get(1).getBookName());
            // 카테고리 확인
            assertEquals(1L, responseDto.getAdminBooksResponseDtos().get(0).getBookCategoryId());
            assertEquals("Fiction", responseDto.getAdminBooksResponseDtos().get(0).getBookCategory().getBookCategoryName());
            // 나머지 필드들도 확인해보기
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