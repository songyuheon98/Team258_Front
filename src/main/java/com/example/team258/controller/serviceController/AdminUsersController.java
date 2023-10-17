package com.example.team258.controller.serviceController;

import com.example.team258.dto.AdminUsersResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUsersController {
    private final AdminUsersService adminUsersService;
    @GetMapping("/users")
    public ResponseEntity<List<AdminUsersResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminUsersService.getAllUsers());
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<MessageDto> deleteUser(@PathVariable Long userId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(adminUsersService.deleteUser(userId, userDetails.getUser()));
    }
}