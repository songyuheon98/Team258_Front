package com.example.team258.controller.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnswerViewController {
    @GetMapping("/answer")
    public String answerView() {
        return "answer";
    }

    @GetMapping("/answerList")
    public String answerListView() {
        return "answerList";
    }
}