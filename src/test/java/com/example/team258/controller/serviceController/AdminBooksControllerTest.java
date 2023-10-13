package com.example.team258.controller.serviceController;

import com.example.team258.dto.AdminBooksRequestDto;
import com.example.team258.dto.AdminBooksResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminBooksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdminBooksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminBooksService adminBooksService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Nested
    @DisplayName("AdminBooksController - CRUD 테스트")
    class LoginnedControllerTest {
        @Test
        @DisplayName("CREATE - 도서 추가 성공")
        void 도서_추가_테스트() throws Exception {
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
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            MessageDto successMessage = MessageDto.builder()
                    .msg("도서 추가가 완료되었습니다.")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            when(adminBooksService.createBook(any(), any()))
                    .thenReturn(new ResponseEntity<>(successMessage, HttpStatus.OK));

            // then
            mockMvc.perform(post("/api/admin/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("도서 추가가 완료되었습니다."));
        }


        @Test
        @DisplayName("CREATE - 일반 유저로 도서 추가 시도 - 권한 없음")
        void 일반_유저로_도서_추가_시도_테스트() throws Exception {
            // given
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Test Book")
                    .bookInfo("Test Book Info")
                    .bookAuthor("Test Author")
                    .bookPublish(LocalDateTime.now())
                    .bookCategoryId(1L)
                    .build();

            User normalUser = User.builder()
                    .userId(2L)
                    .username("user")
                    .password("1234abcd!")
                    .role(UserRoleEnum.USER) // 일반 유저
                    .build();

            // 가상의 일반 유저로 로그인 상태를 설정
            UserDetailsImpl normalUserDetails = new UserDetailsImpl(normalUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(normalUserDetails, null, normalUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when - createBook 메소드가 호출되면 권한 없음 응답을 반환하도록 설정
            when(adminBooksService.createBook(any(), any()))
                    .thenReturn(new ResponseEntity<>(HttpStatus.FORBIDDEN)); // 권한 없음 응답

            // then
            mockMvc.perform(post("/api/admin/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isForbidden()); // 권한 없음 응답을 기대
        }

        @Test
        @DisplayName("READ - 도서 조회 성공")
        void 도서_조회_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            Book book1 = Book.builder()
                    .bookId(1L)
                    .bookName("Book 1")
                    .bookInfo("Book 1 Info")
                    .bookAuthor("Author 1")
                    .bookPublish(LocalDateTime.now())
                    .bookCategory(bookCategory)
                    .build();

            Book book2 = Book.builder()
                    .bookId(2L)
                    .bookName("Book 2")
                    .bookInfo("Book 2 Info")
                    .bookAuthor("Author 2")
                    .bookPublish(LocalDateTime.now())
                    .bookCategory(bookCategory)
                    .build();

            List<AdminBooksResponseDto> booksList = Arrays.asList(new AdminBooksResponseDto(book1), new AdminBooksResponseDto(book2));

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            when(adminBooksService.getAllBooks()).thenReturn(booksList);

            // then
            mockMvc.perform(get("/api/admin/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2))) // 반환된 목록의 크기가 2여야 함
                    .andExpect(jsonPath("$[0].bookId").value(1L)) // 첫 번째 도서의 ID가 1L이어야 함
                    .andExpect(jsonPath("$[0].bookName").value("Book 1")) // 첫 번째 도서의 이름이 "Book 1"이어야 함
                    .andExpect(jsonPath("$[1].bookId").value(2L)) // 두 번째 도서의 ID가 2L이어야 함
                    .andExpect(jsonPath("$[1].bookName").value("Book 2")); // 두 번째 도서의 이름이 "Book 2"이어야 함
        }

        @Test
        @DisplayName("READ SELECT - 도서 선택 조회 성공")
        void 도서_선택_조회_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .build();

            Book selectedBook = Book.builder()
                    .bookId(1L)
                    .bookName("Selected Book")
                    .bookInfo("Selected Book Info")
                    .bookAuthor("Selected Author")
                    .bookPublish(LocalDateTime.now())
                    .bookCategory(bookCategory)
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            when(adminBooksService.getBookById(1L, adminUser)).thenReturn(new AdminBooksResponseDto(selectedBook));

            // then
            mockMvc.perform(get("/api/admin/books/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.bookId").value(1L)) // 반환된 도서의 ID가 1L이어야 함
                    .andExpect(jsonPath("$.bookName").value("Selected Book")) // 반환된 도서의 이름이 "Selected Book"이어야 함
                    .andExpect(jsonPath("$.bookInfo").value("Selected Book Info")) // 반환된 도서의 정보가 "Selected Book Info"이어야 함
                    .andExpect(jsonPath("$.bookAuthor").value("Selected Author")); // 반환된 도서의 작가가 "Selected Author"이어야 함
        }

        @Test
        @DisplayName("UPDATE SELECT - 도서 선택 수정 성공")
        void 도서_선택_수정_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Updated Book")
                    .bookInfo("Updated Book Info")
                    .bookAuthor("Updated Author")
                    .bookPublish(LocalDateTime.now())
                    .bookCategoryId(1L)
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            when(adminBooksService.updateBook(requestDto, 1L, adminUser)).thenReturn(new ResponseEntity<>(new MessageDto("도서 정보가 수정되었습니다."), null, HttpStatus.OK));

            // then
            mockMvc.perform(put("/api/admin/books/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("도서 정보가 수정되었습니다.")); // 반환된 메시지가 기대한 값과 일치하는지 확인
        }

        @Test
        @DisplayName("DELETE SELECT - 도서 선택 삭제 성공")
        void 도서_선택_삭제_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            when(adminBooksService.deleteBook(1L, adminUser)).thenReturn(new ResponseEntity<>(new MessageDto("도서가 삭제되었습니다."), null, HttpStatus.OK));

            // then
            mockMvc.perform(delete("/api/admin/books/1")
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("도서가 삭제되었습니다.")); // 반환된 메시지가 기대한 값과 일치하는지 확인
        }
    }
}
