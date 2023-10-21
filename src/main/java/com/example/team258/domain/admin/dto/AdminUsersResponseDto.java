package com.example.team258.domain.admin.dto;

import com.example.team258.common.entity.User;
import com.example.team258.common.entity.UserRoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminUsersResponseDto {
    private Long userId;
    private String username;
    private UserRoleEnum role;

    public AdminUsersResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
