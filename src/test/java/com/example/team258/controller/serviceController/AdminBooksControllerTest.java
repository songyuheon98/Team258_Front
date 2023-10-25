package com.example.team258.controller.serviceController;

import com.example.team258.common.dto.BooksPageResponseDto;
import com.example.team258.domain.admin.controller.AdminBooksController;
import com.example.team258.domain.admin.dto.AdminBooksRequestDto;
import com.example.team258.domain.admin.dto.AdminBooksResponseDto;
import com.example.team258.common.dto.MessageDto;
import com.example.team258.common.entity.*;
import com.example.team258.domain.admin.repository.AdminBooksRepository;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
import com.example.team258.common.security.UserDetailsImpl;
import com.example.team258.domain.admin.service.AdminBooksService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdminBooksControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminBooksService adminBooksService;

    @MockBean
    private AdminBooksRepository adminBooksRepository;

    @MockBean
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Nested
    @DisplayName("AdminBooksController - CRUD 테스트")
    class LoginnedControllerTest {
        @Test
        @DisplayName("도서 추가 - 성공")
        void 도서_추가_테스트() throws Exception {
            // given
            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Test Book")
                    .bookAuthor("Test Author")
                    .bookPublish("2011")
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

            when(adminBooksService.createBook(any(), any()))
                    .thenReturn(successMessage);

            // when
            mockMvc.perform(post("/api/admin/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("도서 추가가 완료되었습니다."));
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
                    .bookAuthor("Author 1")
                    .bookPublish("2011")
                    .bookCategory(bookCategory)
                    .build();

            Book book2 = Book.builder()
                    .bookId(2L)
                    .bookName("Book 2")
                    .bookAuthor("Author 2")
                    .bookPublish("2011")
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
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[0].bookId").value(1L))
                    .andExpect(jsonPath("$[0].bookName").value("Book 1"))
                    .andExpect(jsonPath("$[1].bookId").value(2L))
                    .andExpect(jsonPath("$[1].bookName").value("Book 2"));
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
                    .bookAuthor("Selected Author")
                    .bookPublish("2011")
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

            // 도서를 데이터베이스에 저장
            Long bookId = 1L;

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
            adminBooksRepository.save(existingBook);

            AdminBooksRequestDto requestDto = AdminBooksRequestDto.builder()
                    .bookName("Updated Book")
                    .bookAuthor("Updated Author")
                    .bookPublish("2011")
                    .bookCategoryId(1L)
                    .bookStatus(BookStatusEnum.IMPOSSIBLE)
                    .build();

            MessageDto successMessage = MessageDto.builder()
                    .msg("도서 정보가 수정되었습니다.")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 모의 객체 설정
            when(adminBooksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
            when(bookCategoryRepository.findById(1L)).thenReturn(Optional.of(bookCategory));
            // when
            when(adminBooksService.updateBook(requestDto, 1L, adminUser))
                    .thenReturn(successMessage);
            // then
            mockMvc.perform(put("/api/admin/books/1")
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isOk());
                    //.andExpect(jsonPath("$.msg").value("도서 정보가 수정되었습니다."));
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
            when(adminBooksService.deleteBook(1L, adminUser))
                    .thenReturn(new MessageDto("도서가 삭제되었습니다."));
            // then
            mockMvc.perform(delete("/api/admin/books/1")
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("도서가 삭제되었습니다.")); // 반환된 메시지가 기대한 값과 일치하는지 확인
        }
    }
}
