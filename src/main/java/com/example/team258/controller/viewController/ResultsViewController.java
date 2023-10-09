package com.example.team258.controller.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResultsViewController {
    @GetMapping("/results")
    public String resultsView() {
        return "results";
    }
}