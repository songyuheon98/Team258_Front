package com.example.team258.controller.serviceController;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.dto.MessageDto;
import com.example.team258.security.UserDetailsImpl;
import com.example.team258.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public AnswerResponseDto createAnswer(@RequestBody AnswerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws InterruptedException {
        return answerService.createAnswer(requestDto, userDetails.getUser());
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponseDto>> getAnswers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(answerService.getAnswers(userDetails.getUser()));
    }

    @PutMapping("/{answerId}")
    public ResponseEntity<MessageDto> updateAnswer(@RequestBody AnswerRequestDto requestDto,@PathVariable Long answerId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws InterruptedException {
        return ResponseEntity.ok(answerService.updateAnswer(requestDto,answerId, userDetails.getUser()));
    }

    @DeleteMapping("/{answerId}")
    public ResponseEntity<MessageDto> deleteAnswer(@PathVariable Long answerId, @AuthenticationPrincipal UserDetailsImpl userDetails) throws InterruptedException {
        return ResponseEntity.ok(answerService.deleteAnswer(answerId,userDetails.getUser()));
    }
}
