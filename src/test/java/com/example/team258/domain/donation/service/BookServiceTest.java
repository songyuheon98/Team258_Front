package com.example.team258.domain.donation.service;

import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.repository.BookRepository;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;
    @Mock
    private BookRepository bookRepository;

    @BeforeAll
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findBookByNameAndRoleAndDonationIdWithPagination() {
        // given
        BooleanBuilder builder = new BooleanBuilder();
        Book book = Book.builder()
                .bookName("bookName")
                .bookAuthor("bookAuthor")
                .bookPublish("bookPublish")
                .bookStatus(BookStatusEnum.valueOf("POSSIBLE"))
                .build();
        List<Book> books = new ArrayList<>();
        books.add(book);

        when(bookRepository.findAll(any(BooleanBuilder.class), any(Pageable.class))).thenReturn(new PageImpl<>(books));

        // when
        Page<Book> result = bookService.findBookByNameAndRoleAndDonationIdWithPagination("bookName", "bookAuthor", "bookPublish", "POSSIBLE", 1L, Pageable.unpaged());

        // then
        assert(result.getTotalElements() == 1);
        assertThat(result.getContent().get(0).getBookName()).isEqualTo("bookName");
        assertThat(result.getContent().get(0).getBookAuthor()).isEqualTo("bookAuthor");
        assertThat(result.getContent().get(0).getBookPublish()).isEqualTo("bookPublish");
        assertThat(result.getContent().get(0).getBookStatus()).isEqualTo(BookStatusEnum.valueOf("POSSIBLE"));

    }
}