package com.example.team258.controller.viewController.admin;

import com.example.team258.dto.*;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.repository.BookRepository;
import com.example.team258.service.BookApplyDonationService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/donation")
@RequiredArgsConstructor
public class DonationViewController {
    private final BookDonationEventService bookDonationEventService;
    private final BookApplyDonationService bookApplyDonationService;
    private final BookRepository bookRepository;

    @GetMapping
    public String donation(Model model) {
        List<BookDonationEventResponseDto> bookDonationEventResponseDtos = bookDonationEventService.getDonationEvent();

        model.addAttribute("events", bookDonationEventResponseDtos);
        return "/admin/donation";
    }

    @GetMapping("/v2")
    public String donationV2(@RequestParam(defaultValue = "0") int page,Model model) {
        PageRequest pageRequest = PageRequest.of(page, 5);  // page 파라미터로 받은 값을 사용

        BookDonationEventPageResponseDto bookDonationEventPageResponseDtos = bookDonationEventService.getDonationEventV2(pageRequest);

        model.addAttribute("events", bookDonationEventPageResponseDtos.getBookDonationEventResponseDtos());
        model.addAttribute("currentPage", page);  // 현재 페이지 번호 추가
        model.addAttribute("totalPages", bookDonationEventPageResponseDtos.getTotalPages());
        return "/admin/donationV2";
    }

    @GetMapping("/v3")
    public String donationV3(@RequestParam(defaultValue = "0") int eventPage,@RequestParam(defaultValue = "0") int[] bookPage,Model model) {
        int eventPagesize =3;
        int bookPagesize = 3;

        PageRequest eventPageRequest = PageRequest.of(eventPage, eventPagesize);  // page 파라미터로 받은 값을 사용
//        PageRequest bookPageRequest = PageRequest.of(bookPage, bookPagesize);  // page 파라미터로 받은 값을 사용


        /**
         * 이벤트 페이징 리스트
         */
        BookDonationEventPageResponseDtoV3 bookDonationEventPageResponseDtoV3 =
                bookDonationEventService.getDonationEventV3(eventPageRequest);


        int eventListSize = bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().size();
        int bookPageTemp[] = new int[eventListSize];

        for (int i = 0;i<bookPage.length;i++)
            bookPageTemp[i] = bookPage[i];


        /**
         * 이벤트에 대한 도서들 페이징 리스트
         */
//        List<Page<Book>> bookPages = bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().stream()
//                .map(bookDonationEventResponseDtoV3 -> bookRepository
//                        .findBooksNoStatusByDonationId(bookDonationEventResponseDtoV3.getDonationId(),bookPageRequest))
//                .toList();
        List<Page<Book>> bookPages = new ArrayList<>();
        for (int i = 0; i <eventListSize; i++) {
            PageRequest bookPageRequest = PageRequest.of(bookPageTemp[i], bookPagesize);
            bookPages.add(bookRepository.findBooksNoStatusByDonationId(
                    bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().get(i).getDonationId(),
                    bookPageRequest));
        }


        /**
         * 이벤트에 대한 도서들 페이징 리스트를 이벤트에 추가
         */
        for (int i = 0; i < bookPages.size(); i++)
            bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3().get(i)
                    .setBookResponseDtos(bookPages.get(i).stream().map(BookResponseDto::new).toList());



        model.addAttribute("events", bookDonationEventPageResponseDtoV3.getBookDonationEventResponseDtoV3());
        model.addAttribute("currentEventPage", eventPage);  // 현재 페이지 번호 추가
        model.addAttribute("currentBookPage", bookPageTemp);  // 현재 페이지 번호 추가
        model.addAttribute("totalPages", bookDonationEventPageResponseDtoV3.getTotalPages());

        return "/admin/donationV3";
    }




    @GetMapping("/bookSetting/{donationId}")
    public String bookSetting(@PathVariable Long donationId, Model model) {
        List<BookResponseDto> bookResponseDtos = bookApplyDonationService.getDonationBooks(BookStatusEnum.POSSIBLE);
        model.addAttribute("books", bookResponseDtos);
        model.addAttribute("donationId", donationId);
        return "/admin/bookSetting";
    }

    @GetMapping("/bookSetting/{donationId}/v2")
    public String bookSettingV2(@RequestParam(defaultValue = "0") int page,@PathVariable Long donationId, Model model) {
        PageRequest pageRequest = PageRequest.of(page, 15);  // page 파라미터로 받은 값을 사용

        BookResponsePageDto bookResponsePageDto = bookApplyDonationService.getDonationBooksV2(BookStatusEnum.POSSIBLE,pageRequest);

        model.addAttribute("books", bookResponsePageDto.getBookResponseDtos());
        model.addAttribute("currentPage",page);
        model.addAttribute("totalPages", bookResponsePageDto.getTotalPages());
        model.addAttribute("donationId", donationId);
        return "/admin/bookSettingV2";
    }
}