package com.example.team258.controller.viewController.user;

import com.example.team258.dto.*;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookDonationEvent;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.repository.BookDonationEventRepository;
import com.example.team258.repository.BookRepository;
import com.example.team258.service.BookDonationEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/users/bookDonationEvent")
@RequiredArgsConstructor
public class BookDonationEventViewController {
    private final BookDonationEventService bookDonationEventService;
    private final BookDonationEventRepository bookDonationEventRepository;
    private final BookRepository bookRepository;

    @GetMapping
    public String bookDonation(Model model) {
        List<BookDonationEventResponseDto> bookResponseDtos = bookDonationEventService.getDonationEvent();
        model.addAttribute("events", bookResponseDtos);

        return "/users/bookDonationEvent";
    }

    /**
     * 이벤트만 가져오는 컨트럴러
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/v2")
    public String bookDonationEventOnlyV2(@RequestParam(defaultValue = "0") int page, Model model) {
        PageRequest pageRequest = PageRequest.of(page, 8);  // page 파라미터로 받은 값을 사용

        BookDonationEventOnlyPageResponseDto bookDonationEventOnlyPageResponseDto = bookDonationEventService.getDonationEventOnlyV2(pageRequest);

        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", bookDonationEventOnlyPageResponseDto.getTotalpages());
        model.addAttribute("events", bookDonationEventOnlyPageResponseDto.getBookDonationEventOnlyResponseDtos());

        return "/users/bookDonationEventV2";
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

    @GetMapping("{donationId}/v2")
    public String bookApplyDonationEventPageV2(@RequestParam(defaultValue = "0") int page,Model model, @PathVariable Long donationId) {
        PageRequest pageRequest = PageRequest.of(page, 10);  // page 파라미터로 받은 값을 사용

        BookDonationEvent bookDonationEvent = bookDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        Page<Book> books = bookRepository.findBooksByDonationId(donationId,BookStatusEnum.DONATION,pageRequest);

        List<BookResponseDto> bookResponseDtos = books.stream()
                .map(BookResponseDto::new)
                .toList();

        BookDonationEventResponseDto bookDonationEventResponseDto = new BookDonationEventResponseDto(bookDonationEvent);

        model.addAttribute("bookDonationEvent", bookDonationEventResponseDto);
        model.addAttribute("books", bookResponseDtos);
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", books.getTotalPages());

        return "/users/bookApplyDonationV2";
    }
}