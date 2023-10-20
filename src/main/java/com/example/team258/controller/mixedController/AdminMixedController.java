package com.example.team258.controller.mixedController;

import com.example.team258.dto.UserResponseDto;
import com.example.team258.entity.User;
import com.example.team258.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class AdminMixedController {
    private final UserRepository userRepository;

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
        Page<User> users = userRepository.findAll(pageRequest);
        int totalPages = users.getTotalPages();

        List<UserResponseDto> userResponseDtos = users.stream().map(UserResponseDto::new).collect(Collectors.toList());

        model.addAttribute("currentPage", page);  // 현재 페이지 번호 추가
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("users", userResponseDtos);

        return "adminV2";
    }

}