package com.example.team258.common.controller.mixedController;

import com.example.team258.domain.member.dto.UserResponseDto;
import com.example.team258.common.entity.User;
import com.example.team258.common.repository.UserRepository;
import com.example.team258.common.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminUsersMixedController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/admin/users")
    /**
     * @RequestParam(defaultValue = "0") int page : page 파라미터가 없으면 0으로 설정
     */
    public String adminView(@RequestParam(defaultValue = "0") int page, Model model) {

        PageRequest pageRequest = PageRequest.of(page, 5);  // page 파라미터로 받은 값을 사용
        Page<User> users = userRepository.findAll(pageRequest);
        int totalPages = users.getTotalPages();

        List<UserResponseDto> userResponseDtos = users.stream().map(UserResponseDto::new).collect(Collectors.toList());

        model.addAttribute("currentPage", page);  // 현재 페이지 번호 추가
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("users", userResponseDtos);

        return "admin";
    }

    @GetMapping("/admin/users/v2")
    /**
     * @RequestParam(defaultValue = "0") int page : page 파라미터가 없으면 0으로 설정
     */
    public String adminViewV2(@RequestParam(defaultValue = "0") int page, Model model, @RequestParam(defaultValue = "") String userName,@RequestParam(defaultValue = "") String userRole ) {
        PageRequest pageRequest = PageRequest.of(page, 5);  // page 파라미터로 받은 값을 사용
        Page<User> users = userService.findUsersByUsernameAndRoleV1(userName, userRole, pageRequest);
        int totalPages = users.getTotalPages();

        List<UserResponseDto> userResponseDtos = users.stream().map(UserResponseDto::new).collect(Collectors.toList());

        model.addAttribute("currentPage", page);  // 현재 페이지 번호 추가
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("users", userResponseDtos);

        return "adminV2";
    }

}