package com.example.team258;


import com.example.team258.common.dto.BookResponseDto;
import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookCategory;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.repository.BookRepository;
import com.example.team258.domain.admin.dto.AdminCategoriesResponseDto;
import com.example.team258.domain.admin.repository.BookCategoryRepository;
import com.example.team258.domain.admin.service.AdminCategoriesService;
import com.example.team258.domain.bookSearch.service.SearchService;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.ArrayList;
import java.util.List;

import static com.example.team258.common.entity.BookStatusEnum.POSSIBLE;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
public class SearchMixedControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SearchService searchService;

    @MockBean
    private AdminCategoriesService adminCategoriesService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private BookCategoryRepository bookCategoryRepository;

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void SearchMixedControllerTestV3() throws Exception {
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
                .bookStatus(POSSIBLE)
                .build();

        List<AdminCategoriesResponseDto> categories = new ArrayList<>();
        categories.add(new AdminCategoriesResponseDto(bookCategory));

        responseDtos.add(new BookResponseDto(book));
        Page<BookResponseDto> bookPage = new PageImpl<>(responseDtos);
        when(adminCategoriesService.getAllCategories()).thenReturn(categories);
        when(searchService.getAllBooksByCategoryOrKeyword3(null,null,0)).thenReturn(bookPage);
        mockMvc.perform(get("/search/v3?page=1"))
                .andExpect(view().name("search"))
                .andExpect(model().attribute("bookMaxCount",1))
                .andExpect(model().attribute("books", hasItem(
                        allOf(
                                hasProperty("bookName", is("과학책")),
                                hasProperty("bookAuthor", is("과학작가"))
                        )
                )))
                .andExpect(model().attribute("categories",hasItem(
                        hasProperty("bookCategoryName", is("과학"))
                        )));
    }

    @Test
    void SearchMixedControllerTestV2() throws Exception {
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
                .bookStatus(POSSIBLE)
                .build();

        List<AdminCategoriesResponseDto> categories = new ArrayList<>();
        categories.add(new AdminCategoriesResponseDto(bookCategory));

        responseDtos.add(new BookResponseDto(book));
        Page<BookResponseDto> bookPage = new PageImpl<>(responseDtos);
        when(adminCategoriesService.getAllCategories()).thenReturn(categories);
        when(searchService.getAllBooksByCategoryOrKeyword2(null,null,0)).thenReturn(bookPage);
        mockMvc.perform(get("/search/v2?page=1"))
                .andExpect(view().name("search"))
                .andExpect(model().attribute("bookMaxCount",1))
                .andExpect(model().attribute("books", hasItem(
                        allOf(
                                hasProperty("bookName", is("과학책")),
                                hasProperty("bookAuthor", is("과학작가"))
                        )
                )))
                .andExpect(model().attribute("categories",hasItem(
                        hasProperty("bookCategoryName", is("과학"))
                )));
    }


}
