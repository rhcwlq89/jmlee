package com.example.demo.controller;


import com.example.demo.ResultDto;
import com.example.demo.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("data")
@RequiredArgsConstructor
@Slf4j
public class TestRestController {
    private final TestService testService;

    @GetMapping
    public Set<ResultDto> request(@RequestParam(defaultValue = "모니터암") String itemName,
                                  @RequestParam(defaultValue = "0") Long index, Model model) {
        return testService.requestNaver(itemName, index);
    }
}
