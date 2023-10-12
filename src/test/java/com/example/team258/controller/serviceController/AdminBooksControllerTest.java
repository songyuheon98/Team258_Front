package com.example.team258.controller.serviceController;

import com.example.team258.dto.AdminBooksRequestDto;
import com.example.team258.dto.MessageDto;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                    .categoryId(1L)
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
                    .categoryId(1L)
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
    }
}
