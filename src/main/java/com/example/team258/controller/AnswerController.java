package com.example.team258.controller;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.entity.MessageDto;
import com.example.team258.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.example.team258.security.UserDetailsImpl;
import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/answer")
    public ResponseEntity<MessageDto> createAnswer(@RequestBody AnswerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(answerService.createAnswer(requestDto, userDetails.getUser()));
    }

    @GetMapping("/answer")
    public ResponseEntity<List<AnswerResponseDto>> getAnswers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(answerService.getAnswers(userDetails.getUser()));
    }

    @PutMapping("/answer/{answerId}")
    public ResponseEntity<MessageDto> updateAnswer(@RequestBody AnswerRequestDto requestDto,@PathVariable Long answerId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(answerService.updateAnswer(requestDto,answerId, userDetails.getUser()));
    }

    @DeleteMapping("/answer/{answerId}")
    public ResponseEntity<MessageDto> deleteAnswer(@PathVariable Long answerId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(answerService.deleteAnswer(answerId,userDetails.getUser()));
    }
}
