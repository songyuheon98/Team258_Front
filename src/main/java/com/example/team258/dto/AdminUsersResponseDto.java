package com.example.team258.dto;

import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
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
