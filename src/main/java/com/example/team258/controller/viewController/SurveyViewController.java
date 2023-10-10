package com.example.team258.controller.viewController;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SurveyViewController {
    @GetMapping("/surveys")
    public String surveyView() {
        return "surveys";
    }

    @GetMapping("/surveysList")
    public String surveyListView(){
        return "surveysList";
    }
}

