package com.example.team258.dto;

import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import lombok.Data;
import org.springframework.data.domain.Pageable;

@Data
public class UserResponseDto {
    private Long userId;
    private String username;
    private UserRoleEnum role;

    public UserResponseDto(User user) {
        this.userId = user.getUserId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

}
