package com.example.team258.service;

import com.example.team258.dto.BookResponseDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.repository.BookCategoryRepository;
import com.example.team258.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final BookRepository bookRepository;
    private final BookCategoryRepository bookCategoryRepository;



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

    public Page<BookResponseDto> getAllBooksByCategory(String bookCategoryName,int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");
        Pageable pageable = PageRequest.of(page,20,sort);

        BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
        List<BookCategory> bookCategories = saveAllCategories(bookCategory);

        Page<Book> bookList = bookRepository.findAllByCategories(bookCategories,pageable);

        return bookList.map(BookResponseDto::new);
    }

    public Page<BookResponseDto> getAllBooksByKeyword(String keyword, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");

        Pageable pageable = PageRequest.of(page,20,sort);

        Page<Book> bookList = bookRepository.findAllByBookNameContaining(keyword, pageable);
        return bookList.map(BookResponseDto::new);
    }

    public Page<BookResponseDto> getAllBooksByCategoryOrKeyword(String bookCategoryName, String keyword, int page) {
        Sort sort = Sort.by(Sort.Direction.ASC,"bookId");
        Pageable pageable = PageRequest.of(page,20,sort);

        BookCategory bookCategory = bookCategoryRepository.findByBookCategoryName(bookCategoryName);
        List<BookCategory> bookCategories = saveAllCategories(bookCategory);

        Page<Book> bookList = bookRepository.findAllByCategoriesAndBookNameContaining(bookCategories, keyword, pageable);
        return bookList.map(BookResponseDto::new);
    }


    private List<BookCategory> saveAllCategories(BookCategory bookCategory){
        BookCategory category = bookCategory;
        List<BookCategory> answer = new ArrayList<>();
        answer.add(category);
        if (category.getChildCategories().isEmpty()) return answer;
        for(BookCategory tmp : category.getChildCategories()){
            answer.addAll(saveAllCategories(tmp));
        }
        return answer;
    }

}
