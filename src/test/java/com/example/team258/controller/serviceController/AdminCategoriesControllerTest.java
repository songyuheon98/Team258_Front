package com.example.team258.controller.serviceController;

import com.example.team258.dto.AdminCategoriesRequestDto;
import com.example.team258.dto.AdminCategoriesResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.AdminBooksRepository;
import com.example.team258.repository.BookCategoryRepository;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminCategoriesService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdminCategoriesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminCategoriesService adminCategoriesService;

    @MockBean
    private BookCategoryRepository bookCategoryRepository;

    @MockBean
    private AdminBooksRepository adminBooksRepository;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("AdminCategoryController - CRUD 테스트")
    class AdminCategoryCrudTest {

        @Test
        @DisplayName("CREATE - 카테고리 추가 성공")
        void 카테고리_추가_테스트() throws Exception {
            // given
            AdminCategoriesRequestDto requestDto = AdminCategoriesRequestDto.builder()
                    .bookCategoryName("Test Category")
                    .build();

            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            MessageDto successMessage = MessageDto.builder()
                    .msg("카테고리 추가가 완료되었습니다.")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);


            when(adminCategoriesService.createBookCategory(any(), any()))
                    .thenReturn(new ResponseEntity<>(successMessage, HttpStatus.OK));

            // when
            mockMvc.perform(post("/api/admin/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("카테고리 추가가 완료되었습니다."));

            // then
            verify(adminCategoriesService, times(1)).createBookCategory(any(), any());
        }


        @Test
        @DisplayName("CREATE SubCategory - 서브 카테고리 추가 성공")
        void 서브_카테고리_추가_테스트() throws Exception {
            // given
            AdminCategoriesRequestDto requestDto = AdminCategoriesRequestDto.builder()
                    .bookCategoryName("SubCategory")
                    .build();

            Long parentId = 1L;

            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            MessageDto successMessage = MessageDto.builder()
                    .msg("서브 카테고리 추가가 완료되었습니다.")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(adminCategoriesService.createSubBookCategory(eq(parentId), any(), any()))
                    .thenReturn(new ResponseEntity<>(successMessage, HttpStatus.OK));

            // when
            mockMvc.perform(post("/api/admin/categories/{parentId}/subcategories", parentId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("서브 카테고리 추가가 완료되었습니다."));

            // then
            verify(adminCategoriesService, times(1)).createSubBookCategory(eq(parentId), any(), any());
        }

        @Test
        @DisplayName("READ All Categories - 전체 카테고리 조회 성공")
        void 전체_카테고리_조회_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            BookCategory bookCategory1 = BookCategory.builder()
                    .bookCategoryName("카테고리1")
                    .bookCategoryId(1L)
                    .build();

            BookCategory bookCategory2 = BookCategory.builder()
                    .bookCategoryName("카테고리2")
                    .bookCategoryId(2L)
                    .build();

            List<AdminCategoriesResponseDto> categoriesList = Arrays.asList(new AdminCategoriesResponseDto(bookCategory1), new AdminCategoriesResponseDto(bookCategory2));

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(adminCategoriesService.getAllCategories()).thenReturn(categoriesList);

            // when
            mockMvc.perform(get("/api/admin/categories")
                            .contentType(MediaType.APPLICATION_JSON)
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2))) // 반환된 목록의 크기가 2여야 함
                    .andExpect(jsonPath("$[0].categoryId").value(1L)) // 첫 번째 카테고리의 ID가 1L이어야 함
                    .andExpect(jsonPath("$[0].bookCategoryName").value("카테고리1")) // 첫 번째 카테고리의 이름이 "카테고리1"이어야 함
                    .andExpect(jsonPath("$[1].categoryId").value(2L)) // 두 번째 카테고리의 ID가 2L이어야 함
                    .andExpect(jsonPath("$[1].bookCategoryName").value("카테고리2")); // 두 번째 카테고리의 이름이 "카테고리2"이어야 함

            // then
            verify(adminCategoriesService, times(1)).getAllCategories();
        }

        @Test
        @DisplayName("UPDATE Category Name - 카테고리 이름 수정 성공")
        void 카테고리_이름_수정_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            Long bookCategoryId = 1L;

            BookCategory existingBookCategory = BookCategory.builder()
                    .bookCategoryId(1L)
                    .bookCategoryName("카테고리1")
                    .build();

            bookCategoryRepository.save(existingBookCategory);

            AdminCategoriesRequestDto requestDto = AdminCategoriesRequestDto.builder()
                    .bookCategoryName("수정된카테고리1")
                    .build();

            MessageDto successMessage = MessageDto.builder()
                    .msg("카테고리 이름이 수정되었습니다.")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(bookCategoryRepository.findById(bookCategoryId)).thenReturn(Optional.of(existingBookCategory));

            // when
            when(adminCategoriesService.updateBookCategory(1L, requestDto, adminUser))
                    .thenReturn(new ResponseEntity<>(successMessage, HttpStatus.OK));

            // then
            mockMvc.perform(put("/api/admin/categories/{bookCategoryId}", bookCategoryId)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .content(objectMapper.writeValueAsString(requestDto))
                            .principal(authentication))
                    .andExpect(status().isOk());
            //.andExpect(jsonPath("$.msg").value("카테고리 이름이 수정되었습니다."));

        }

        @Test
        @DisplayName("UPDATE Book's Category - 도서 카테고리 수정 성공")
        void 도서_카테고리_수정_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            Long bookId = 1L;
            Long categoryId = 2L; // 새로운 카테고리 ID

            Book existingBook = Book.builder()
                    .bookId(bookId)
                    .bookName("Test Book")
                    .bookAuthor("Test Author")
                    .bookPublish(LocalDateTime.now())
                    .bookCategory(BookCategory.builder().bookCategoryId(1L).bookCategoryName("Old Category").build())
                    .build();

            BookCategory newCategory = BookCategory.builder()
                    .bookCategoryId(categoryId)
                    .bookCategoryName("New Category")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(adminCategoriesService.updateBookCategory(bookId, categoryId, adminUserDetails.getUser()))
                    .thenReturn(new ResponseEntity<>(new MessageDto("도서의 카테고리가 수정되었습니다."), null, HttpStatus.OK));

            when(adminBooksRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
            when(bookCategoryRepository.findById(categoryId)).thenReturn(Optional.of(newCategory));

            // when
            mockMvc.perform(put("/api/admin/categories/books/{bookId}/categories/{categoryId}", bookId, categoryId)
                            .contentType(MediaType.APPLICATION_JSON_UTF8)
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("도서의 카테고리가 수정되었습니다."));

            // then
            verify(adminCategoriesService, times(1)).updateBookCategory(bookId, categoryId, adminUserDetails.getUser());
        }

        @Test
        @DisplayName("DELETE Category - 카테고리 삭제 성공")
        void 카테고리_삭제_테스트() throws Exception {
            // given
            User adminUser = User.builder()
                    .userId(1L)
                    .username("admin")
                    .password("1234abcd!")
                    .role(UserRoleEnum.ADMIN)
                    .build();

            Long bookCategoryId = 1L;

            BookCategory existingCategory = BookCategory.builder()
                    .bookCategoryId(bookCategoryId)
                    .bookCategoryName("Test Category")
                    .build();

            MessageDto successMessage = MessageDto.builder()
                    .msg("카테고리가 삭제되었습니다.")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            when(adminCategoriesService.deleteBookCategory(bookCategoryId, adminUserDetails.getUser()))
                    .thenReturn(new ResponseEntity<>(successMessage, null, HttpStatus.OK));

            when(bookCategoryRepository.findById(bookCategoryId)).thenReturn(Optional.of(existingCategory));

            // when
            mockMvc.perform(delete("/api/admin/categories/{bookCategoryId}", bookCategoryId)
                            .principal(authentication))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("카테고리가 삭제되었습니다."));

            // then
            verify(adminCategoriesService, times(1)).deleteBookCategory(bookCategoryId, adminUserDetails.getUser());
        }

    }
}