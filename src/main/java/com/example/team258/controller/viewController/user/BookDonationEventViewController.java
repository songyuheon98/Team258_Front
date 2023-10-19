package com.example.team258.controller.viewController.user;

import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.dto.BookResponseDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookDonationEvent;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.repository.BookDonationEventRepository;
import com.example.team258.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users/bookDonationEvent")
@RequiredArgsConstructor
public class BookDonationEventViewController {
    private final BookDonationEventService bookDonationEventService;
    private final BookDonationEventRepository bookDonationEventRepository;
    @GetMapping
    public String bookApplyDonation(Model model) {
        List<BookDonationEventResponseDto> bookResponseDtos = bookDonationEventService.getDonationEvent();
        model.addAttribute("events", bookResponseDtos);

        return "/users/bookDonationEvent";
    }

    @GetMapping("{donationId}")
    public String bookApplyDonationEventPage(Model model, @PathVariable Long donationId) {
        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        List<Book> books = bookDonationEvent.getBooks().stream().filter(book -> book.getBookStatus().equals(BookStatusEnum.DONATION)).toList();

        List<BookResponseDto> bookResponseDtos = books.stream()
                .map(BookResponseDto::new)
                .toList();
        BookDonationEventResponseDto bookDonationEventResponseDto = new BookDonationEventResponseDto(bookDonationEvent);

        model.addAttribute("bookDonationEvent", bookDonationEventResponseDto);
        model.addAttribute("books", bookResponseDtos);

        return "/users/bookApplyDonation";
    }

}