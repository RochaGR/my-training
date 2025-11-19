package com.senai.projeto.mytraining.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class TestController {

    @GetMapping("/ping")
    public String ping() {
        return "PONG! API funcionando! 🎉";
    }
}