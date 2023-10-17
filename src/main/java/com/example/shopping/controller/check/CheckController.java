package com.example.shopping.controller.check;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckController {
    @GetMapping("/")
    public String check() {
        return "성공";
    }
}
