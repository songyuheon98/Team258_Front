package com.example.team258.controller.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnswerViewController {
    @GetMapping("/answers")
    public String answerView() {
        return "answers";
    }

    @GetMapping("/answersList")
    public String answerListView() {
        return "answersList";
    }
}