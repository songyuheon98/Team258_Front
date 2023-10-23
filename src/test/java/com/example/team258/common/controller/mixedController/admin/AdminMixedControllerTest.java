package com.example.team258.common.controller.mixedController.admin;

import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.common.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

//@WebMvcTest(AdminUsersMixedController.class)
//@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class AdminMixedControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Read 관리자 - 유저 관리 페이지 테스트")
    void adminViewV2() throws Exception {
        // given
        User user = User.builder()
                .username("test")
                .password("test")
                .role(UserRoleEnum.USER)
                .build();

        Page<User> users = new PageImpl<>(List.of(user));

        // when
        when(userService.findUsersByUsernameAndRoleV1(any(String.class), any(String.class), any(PageRequest.class))).thenReturn(users);

        // then
        mockMvc.perform(get("/admin/users/v2"))
                .andExpect(status().isOk())
                .andExpect(view().name("adminV2"));
    }
}