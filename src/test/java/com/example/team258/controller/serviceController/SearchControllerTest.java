package com.example.team258.controller.serviceController;

import com.example.team258.dto.BookResponseDto;
import com.example.team258.entity.Book;
import com.example.team258.entity.BookCategory;
import com.example.team258.entity.BookStatusEnum;
import com.example.team258.repository.BookCategoryRepository;
import com.example.team258.repository.BookRepository;
import com.example.team258.service.BookReservationService;
import com.example.team258.service.SearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void GetAllBooks() throws Exception {
        List<BookResponseDto> responseDtos = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        responseDtos.add(new BookResponseDto(book));
        Page<BookResponseDto> bookPage = new PageImpl<>(responseDtos);
        when(searchService.getAllBooks(0)).thenReturn(bookPage);
        mockMvc.perform(get("/api/books?page=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookName").value("과학책"))
                .andExpect(jsonPath("$.content[0].bookAuthor").value("과학작가"))
                .andDo(print());
    }

    @Test
    void GetOneBook() throws Exception {
        List<BookResponseDto> responseDtos = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();

        when(searchService.getBookById(1L)).thenReturn(new BookResponseDto(book));
        mockMvc.perform(get("/api/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookName").value("과학책"))
                .andExpect(jsonPath("$.bookAuthor").value("과학작가"))
                .andDo(print());
    }

    @Test
    void getAllBooksByKeyword() throws Exception {
        List<BookResponseDto> responseDtos = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        responseDtos.add(new BookResponseDto(book));
        Page<BookResponseDto> bookPage = new PageImpl<>(responseDtos);
        when(searchService.getAllBooksByKeyword("과학",0)).thenReturn(bookPage);
        mockMvc.perform(get("/api/books/search?page=1&keyword=과학"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookName").value("과학책"))
                .andExpect(jsonPath("$.content[0].bookAuthor").value("과학작가"))
                .andDo(print());
    }

    @Test
    void getAllBooksByCategory() throws Exception {
        List<BookResponseDto> responseDtos = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        responseDtos.add(new BookResponseDto(book));
        Page<BookResponseDto> bookPage = new PageImpl<>(responseDtos);
        when(searchService.getAllBooksByCategory("과학",0)).thenReturn(bookPage);
        mockMvc.perform(get("/api/books/search?page=1&bookCategoryName=과학"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookName").value("과학책"))
                .andExpect(jsonPath("$.content[0].bookAuthor").value("과학작가"))
                .andDo(print());
    }

    @Test
    void getAllBooksByCategoryOrKeyword() throws Exception {
        List<BookResponseDto> responseDtos = new ArrayList<>();
        BookCategory bookCategory = BookCategory.builder()
                .bookCategoryId(1L)
                .bookCategoryName("과학")
                .build();
        Book book = Book.builder()
                .bookId(1L)
                .bookPublish("2011")
                .bookName("과학책")
                .bookCategory(bookCategory)
                .bookAuthor("과학작가")
                .bookStatus(BookStatusEnum.POSSIBLE)
                .build();
        responseDtos.add(new BookResponseDto(book));
        Page<BookResponseDto> bookPage = new PageImpl<>(responseDtos);
        when(searchService.getAllBooksByCategoryOrKeyword("과학","학",0)).thenReturn(bookPage);
        mockMvc.perform(get("/api/books/search?page=1&bookCategoryName=과학&keyword=학"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].bookName").value("과학책"))
                .andExpect(jsonPath("$.content[0].bookAuthor").value("과학작가"))
                .andDo(print());
    }


}
