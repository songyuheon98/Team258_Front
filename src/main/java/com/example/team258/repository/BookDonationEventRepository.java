package com.example.team258.repository;

import com.example.team258.entity.BookDonationEvent;
import com.example.team258.entity.BookStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookDonationEventRepository extends JpaRepository<BookDonationEvent, Long> {
    Page<BookDonationEvent> findAll(Pageable pageable);

    /**
     * event와 book이 1대다관계라서 패치 조인하면 메모리 레벨에서의 페이징이 발생할 수 있기에
     * 사용하지 않는다.
     */
//    @Query(value = "select bde from book_donation_event bde" +
//            " join fetch bde.books book" +
//            " where bde.donationId = :donationId and book.bookStatus = :status")
//    Page<BookDonationEvent> findPageByDonationId(@Param("donationId") Long donationId, @Param("status") BookStatusEnum status, Pageable pageable);

}
