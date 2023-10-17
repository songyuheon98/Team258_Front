package com.example.team258.controller.mixedController;

import com.example.team258.repository.AdminBooksRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminBooksMixedController {
    private final AdminBooksRepository adminBooksRepository;

    @GetMapping("/admin/books")
    public String adminBooksView() {
        return "/admin/adminBooks";
    }
}