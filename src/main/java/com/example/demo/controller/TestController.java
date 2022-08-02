package com.example.demo.controller;

import com.example.demo.ResultDto;
import com.example.demo.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

@Controller
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final TestService testService;

    @GetMapping
    public String request(@RequestParam String itemName, Model model) {
        log.info(itemName);
        Set<String> strings = testService.requestNaver(itemName);
        strings.forEach(value -> log.info(value));
        model.addAttribute("results", strings);
        return "index";
    }

    @GetMapping("/next")
    public String next (@RequestParam(defaultValue = "모니터암") String keyword, @RequestParam(defaultValue = "31") Long start) {
        testService.requestNext(keyword, start);
        return "index";
    }
}
