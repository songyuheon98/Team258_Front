package com.example.team258.controller.viewController.admin;

import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.BookResponseDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.repository.BookApplyDonationRepository;
import com.example.team258.repository.BookRepository;
import com.example.team258.service.BookApplyDonationService;
import com.example.team258.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/donation")
@RequiredArgsConstructor
public class DonationViewController {
    private final BookDonationEventService bookDonationEventService;
    private final BookApplyDonationService bookApplyDonationService;

    @GetMapping
    public String donation(Model model) {
        List<BookDonationEventResponseDto> bookDonationEventResponseDtos = bookDonationEventService.getDonationEvent();
        model.addAttribute("events", bookDonationEventResponseDtos);
        return "/admin/donation";
    }

    @GetMapping("/bookSetting/{donationId}")
    public String bookSetting(@PathVariable Long donationId, Model model) {
        List<BookResponseDto> bookResponseDtos = bookApplyDonationService.getDonationBooks(BookStatusEnum.POSSIBLE);
        model.addAttribute("books", bookResponseDtos);
        model.addAttribute("donationId", donationId);
        return "/admin/bookSetting";
    }
}