package com.example.team258.controller.viewController.admin;

import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.UserResponseDto;
import com.example.team258.repository.BookDonationEventRepository;
import com.example.team258.repository.UserRepository;
import com.example.team258.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/donation")
@RequiredArgsConstructor
public class DonationController {
    private final BookDonationEventService bookDonationEventService;


    @GetMapping
    public String donation(Model model) {
        List<BookDonationEventResponseDto> bookDonationEventResponseDtos = bookDonationEventService.getDonationEvent();
        model.addAttribute("events", bookDonationEventResponseDtos);
        return "/admin/donation";
    }


}