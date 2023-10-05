package com.example.team258.controller;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/answer")
    public ResponseEntity<String> createAnswer(@RequestBody AnswerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseEntity<String> answer = answerService.createAnswer(requestDto,userDetails.getUser());
        return answer;
    }

    @GetMapping("/answer")
    public List<AnswerResponseDto> getAnswers(@AuthenticationPrincipal UserDetailsImpl userDetails){
        List<AnswerResponseDto> answer = answerService.getAnswers(userDetails.getUser());
        return answer;
    }

    @PutMapping("/answer/{answerId}")
    public ResponseEntity<String> updateAnswer(@RequestBody AnswerRequestDto requestDto,@PathVariable Long answerId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseEntity<String> answer = answerService.updateAnswer(requestDto, answerId,userDetails.getUser());
        return answer;
    }


}
