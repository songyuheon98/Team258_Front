package com.example.team258.controller.serviceController;

import com.example.team258.dto.UserSignupRequestDto;
import com.example.team258.entity.MessageDto;
import com.example.team258.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MessageDto> signup(@Valid @RequestBody UserSignupRequestDto requestDto){
        return userService.signup(requestDto);
    }

    @DeleteMapping("/escape")
    public ResponseEntity<MessageDto> escape(){
        ResponseEntity<MessageDto> escape_result= userService.escape();
        return escape_result;

    }
}
