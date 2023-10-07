package com.example.team258.controller.viewController;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SurveyViewController {
    @GetMapping("/survey")
    public String surveyView() {
        return "survey";
    }

    @GetMapping("/surveyList")
    public String surveyListView(){
        return "surveyList";
    }
}

