package com.example.team258.common.controller.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutUsViewController {
    @GetMapping("/about258")
    public String aboutUs(){
        return "about258";
    }
}