package com.example.team258.controller;

import com.example.team258.dto.AnswerRequestDto;
import com.example.team258.dto.AnswerResponseDto;
import com.example.team258.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("/answer")
    public ResponseEntity<String> createAnswer(@RequestBody AnswerRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){ //@AuthenticationPrincipal은 class확인 후 추가
        ResponseEntity<String> answer = answerService.createAnswer(requestDto,userDetails.getUser());
        return answer;
    }

}
