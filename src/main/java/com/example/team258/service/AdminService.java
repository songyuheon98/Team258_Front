package com.example.team258.service;

import com.example.team258.dto.AdminResponseDto;
import com.example.team258.entity.MessageDto;
import com.example.team258.entity.User;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<AdminResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(AdminResponseDto::new).toList();
    }

    @Transactional
    public MessageDto deleteUser(Long userId, User loginUser) {
        User user = getUserById(userId);
        if (!loginUser.equals(user) && loginUser.getRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("회원을 삭제할 권한이 없습니다.");
        }
        userRepository.delete(user);
        return new MessageDto("삭제가 완료되었습니다");
    }


    private User getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        return user;
    }
}