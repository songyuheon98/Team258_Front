package com.example.team258.domain.donation.service;

import com.example.team258.common.entity.Book;
import com.example.team258.common.entity.BookStatusEnum;
import com.example.team258.common.entity.QBook;
import com.example.team258.common.repository.BookRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    //페이징, 키워드검색도 컨벤션 참고
    public Page<Book> findBookByNameAndRoleAndDonationIdWithPagination(String bookName, String author, String publish, String status,
                                                                       Long donationId, Pageable pageable) {
        QBook qBook = QBook.book;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(qBook.bookDonationEvent.donationId.eq(donationId));
        if(!bookName.isEmpty()|| bookName.equals("null"))
            builder.and(qBook.bookName.contains(bookName));
        if(!author.isEmpty()||author.equals("null"))
            builder.and(qBook.bookAuthor.contains(author));
        if(!publish.isEmpty()||publish.equals("null"))
            builder.and(qBook.bookPublish.contains(publish));
        if(!status.isEmpty()||status.equals("null"))
            builder.and(qBook.bookStatus.eq(BookStatusEnum.valueOf(status)));

        Page<Book> books = bookRepository.findAll(builder,pageable);
        return books;
    }
}
