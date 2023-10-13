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
                    .bookInfo("Test Book Info")
                    .bookAuthor("Test Author")
                    .bookPublish(LocalDateTime.now())
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
        @DisplayName("GET - 도서 조회 성공")
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
                    .bookInfo("Existing Book Info")
                    .bookAuthor("Existing Author")
                    .bookPublish(LocalDateTime.now())
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
                    () -> assertEquals(existingBook.getBookInfo(), responseDto.getBookInfo()),
                    () -> assertEquals(existingBook.getBookAuthor(), responseDto.getBookAuthor()),
                    () -> assertEquals(existingBook.getBookPublish(), responseDto.getBookPublish()),
                    () -> assertEquals(existingBook.getBookCategory().getBookCategoryId(), responseDto.getBookCategory().getBookCategoryId())
            );
        }


        @Test
        @DisplayName("UPDATE - 도서 수정 성공")
        void 도서_업데이트_테스트() {
            // given
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Updated Book")
                    .bookInfo("Updated Book Info")
                    .bookAuthor("Updated Author")
                    .bookPublish(LocalDateTime.now())
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
                    .bookInfo("Existing Book Info")
                    .bookAuthor("Existing Author")
                    .bookPublish(LocalDateTime.now())
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


//@Test
//@DisplayName("리뷰 수정 성공")
//public void testUpdateReview_Success() throws NoSuchFieldException, IllegalAccessException {
//    // 사용자 생성
//    User user = new User("testUser", "password",  UserRoleEnum.ADMIN);
//    Field idFieldUser = User.class.getDeclaredField("id");
//    idFieldUser.setAccessible(true);
//    idFieldUser.set(user , 1L);
//
//
//    // 다른 사용자 생성
//    User user2 = new User("testUser", "password",  UserRoleEnum.USER);
//    Field idFieldUser2 = User.class.getDeclaredField("id");
//    idFieldUser2.setAccessible(true);
//    idFieldUser2.set(user , 2L);
//
//    // 가게와 메뉴 생성
//    BookCategory category = new BookCategory();
//
//    // 리뷰 생성
//    AdminBooksRequestDto existingBookData = new AdminBooksRequestDto(/* 기존 데이터 */);
//    Book book = new Book(existingBookData, category);
//    Long bookId = 1L;
//
//    when(adminBooksRepository.findById(bookId)).thenReturn(Optional.of(book)); // 기존 리뷰 찾는 로직
//
//    // 수정된 매개변수 삽입
//    AdminBooksRequestDto updateData = new AdminBooksRequestDto(/* 수정할 데이터 */);
//
//    assertDoesNotThrow(() -> {
//        MessageDto response = adminBooksService.updateBook(updateData,1L,user).getBody();
//        assertEquals("리뷰가 수정되었습니다.", response.getMsg());
//    });
//}