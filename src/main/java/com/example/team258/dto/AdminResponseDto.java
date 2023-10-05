package com.example.team258.dto;

import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminResponseDto {
    private Long userId;
    private String username;
    private UserRoleEnum role;

    public AdminResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}
