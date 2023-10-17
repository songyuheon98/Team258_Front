package com.example.team258.controller.viewController.user;

import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.BookResponseDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.service.BookApplyDonationService;
import com.example.team258.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users/bookApplyDonation")
@RequiredArgsConstructor
public class BookApplyDonationViewController {
        private final BookApplyDonationService bookApplyDonationService;
        private final BookDonationEventService bookDonationEventService;



        @GetMapping
        public String bookApplyDonation(Model model) {
            List<BookResponseDto> bookResponseDtos = bookApplyDonationService.getDonationBooks(BookStatusEnum.DONATION);

            model.addAttribute("books", bookResponseDtos);

            return "/users/bookApplyDonation";
        }
}
