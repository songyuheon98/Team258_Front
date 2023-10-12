package com.example.team258.search;

import com.example.team258.entity.Book;
import com.example.team258.entity.UserRoleEnum;
import com.example.team258.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BookRepository bookRepository;


    public Page<BookResponseDto> getAllBooks(int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");
        Pageable pageable = PageRequest.of(page,20,sort);

        Page<Book> bookList = bookRepository.findAll(pageable);
        return bookList.map(BookResponseDto::new);
    }

    public BookResponseDto getBookById(Long bookId) {
        Book book =bookRepository.findById(bookId).orElseThrow(()->new NullPointerException("오류"));
        return new BookResponseDto(book);
    }



}
