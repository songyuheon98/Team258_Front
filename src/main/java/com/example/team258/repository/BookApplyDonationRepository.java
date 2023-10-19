package com.example.team258.repository;

import com.example.team258.dto.BookDonationEventResponseDto;
import com.example.team258.entity.BookApplyDonation;
import com.example.team258.entity.BookDonationEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookApplyDonationRepository extends JpaRepository<BookApplyDonation, Long> {
/**
 * 한 사람이 수천 건의 책을 나눔 신청하는 건 불가능 -> 페이징 처리까진 필요 없을 듯
 */
//
//    @Query(value = "select bde from book_apply_donation bde" +
//            " join fetch bde.book book" +
//            " where bde. = :donationId and book.bookStatus = :status")
//    Page<BookApplyDonation> findPageByUserId(Pageable pageable, Long userId);

}
