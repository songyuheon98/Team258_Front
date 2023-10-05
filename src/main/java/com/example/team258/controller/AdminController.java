package com.example.team258.controller;

import com.example.team258.dto.AdminResponseDto;
import com.example.team258.entity.MessageDto;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AdminService;
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
public class AdminController {
    private final AdminService adminService;
    @GetMapping
    public ResponseEntity<List<AdminResponseDto>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<MessageDto> deleteUser(@PathVariable Long userId,
                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(adminService.deleteUser(userId, userDetails.getUser()));
    }
}