package com.example.team258.service;

import com.example.team258.dto.BookDonationEventRequestDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.entity.BookDonationEvent;
import com.example.team258.entity.User;
import com.example.team258.jwt.SecurityUtil;
import com.example.team258.repository.AdminDonationEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminDonationEventService {
    private final AdminDonationEventRepository adminDonationEventRepository;

    @Transactional
    public ResponseEntity<MessageDto> createDonationEvent(BookDonationEventRequestDto bookDonationEventRequestDto) {
        BookDonationEvent bookDonationEvent = new BookDonationEvent(bookDonationEventRequestDto);
        adminDonationEventRepository.save(bookDonationEvent);
        return ResponseEntity.ok(new MessageDto("이벤트추가가 완료되었습니다"));
    }


    @Transactional
    public ResponseEntity<MessageDto> updateDonationEvent(Long donationId, BookDonationEventRequestDto bookDonationEventRequestDto) {
        BookDonationEvent bookDonationEvent = adminDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        bookDonationEvent.update(bookDonationEventRequestDto);
        return ResponseEntity.ok(new MessageDto("이벤트 수정이 완료되었습니다"));
    }

    @Transactional
    public ResponseEntity<MessageDto> deleteDonationEvent(Long donationId) {
        User user = SecurityUtil.getPrincipal().get();
        if(!user.getRole().getAuthority().equals("ROLE_ADMIN")){
            return ResponseEntity.badRequest().body(new MessageDto("관리자만 이벤트를 삭제할 수 있습니다."));
        }
        BookDonationEvent bookDonationEvent = adminDonationEventRepository.findById(donationId).orElseThrow(
                () -> new IllegalArgumentException("해당 이벤트가 존재하지 않습니다.")
        );
        adminDonationEventRepository.delete(bookDonationEvent);
        return ResponseEntity.ok(new MessageDto("이벤트 삭제가 완료되었습니다"));
    }
}
