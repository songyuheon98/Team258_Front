package com.example.team258.repository;

import com.example.team258.entity.BookDonationEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDonationEventRepository extends JpaRepository<BookDonationEvent, Long> {
}
