package com.example.team258.controller;

import com.example.team258.entity.MessageDto;
import com.mysql.cj.protocol.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sample")
@RequiredArgsConstructor
public class SampleController {
//    private final SampleRepository sampleRepository;
//    private final SampleService sampleService;
//    @GetMapping("/sample")
//    public ResponseEntity<MessageDto> sample(){
//
//        return ResponseEntity.ok(sampleService.sample());
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(sampleService.sample());
//    }

}
