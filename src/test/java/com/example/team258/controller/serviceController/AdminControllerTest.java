package com.example.team258.controller.serviceController;

import com.example.team258.dto.AdminResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    User user1 = User.builder()
            .userId(1L)
            .username("user1")
            .password("1234")
            .role(UserRoleEnum.USER)
            .build();

    User user2 = User.builder()
            .userId(2L)
            .username("user2")
            .password("1234")
            .role(UserRoleEnum.USER)
            .build();

    User adminUser = User.builder()
            .userId(3L)
            .username("admin")
            .password("1234")
            .role(UserRoleEnum.ADMIN)
            .build();

    UserDetails authenticateUser(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return userDetails;
    }

    @Nested
    @DisplayName("AdminController - 단순 로직 작동 여부 테스트")
    class BasicControllerReadTest {
        @Test
        @DisplayName("단순_회원조회_테스트")
        void 단순_회원조회_테스트() throws Exception {
            // given
            List<AdminResponseDto> userList = new ArrayList<>();
            userList.add(new AdminResponseDto(user1));
            userList.add(new AdminResponseDto(user2));
            userList.add(new AdminResponseDto(adminUser));

            // when
            when(adminService.getAllUsers())
                    .thenReturn(userList);

            // then
            mockMvc.perform(get("/api/admin/users"))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("단순_회원삭제_테스트")
        void 단순_회원삭제_테스트() throws Exception {
            // given
            MessageDto msg = MessageDto.builder()
                    .msg("회원 삭제 성공")
                    .build();

            // when
            when(adminService.deleteUser(user1.getUserId(), user1)).thenReturn(msg);

            // then
            mockMvc.perform(delete("/api/admin/users/{userId}", user1.getUserId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authenticateUser(user1))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("회원 삭제 성공"));
        }
    }

    @Nested
    @DisplayName("AdminController - 로그인된 회원의 ROLE에 대한 테스트")
    class LoginnedControllerTest {
        @Test
        @DisplayName("회원 조회 성공 - ADMIN으로 로그인한 경우에 성공")
        void 관리자_회원목록조회_테스트() throws Exception {
            // given
            List<AdminResponseDto> userList = new ArrayList<>();
            userList.add(new AdminResponseDto(user1));
            userList.add(new AdminResponseDto(user2));
            userList.add(new AdminResponseDto(adminUser));

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            when(adminService.getAllUsers())
                    .thenReturn(userList);

            // then
            mockMvc.perform(get("/api/admin/users"))
                    .andExpect(status().isOk())
                    .andDo(print());
        }

        @Test
        @DisplayName("회원 삭제 성공 - ADMIN으로 로그인한 경우에 성공")
        void 관리자_회원삭제_테스트() throws Exception {
            // given
            MessageDto msg = MessageDto.builder()
                    .msg("관리자 권한 회원 삭제 성공")
                    .build();

            // 가상의 ADMIN으로 로그인 상태를 설정
            UserDetailsImpl adminUserDetails = new UserDetailsImpl(adminUser);
            Authentication authentication = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // when
            when(adminService.deleteUser(user1.getUserId(), adminUser)).thenReturn(msg);

            // then
            mockMvc.perform(delete("/api/admin/users/{userId}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(authenticateUser(adminUser))))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("관리자 권한 회원 삭제 성공"));
        }
    }
}