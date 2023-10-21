package com.example.team258.domain.donation.repository;

import com.example.team258.domain.donation.entity.BookDonationEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDonationEventRepository extends JpaRepository<BookDonationEvent, Long>, QuerydslPredicateExecutor<BookDonationEvent>   {
    Page<BookDonationEvent> findAll(Pageable pageable);
}
