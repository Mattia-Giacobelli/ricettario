package com.example.ricettario.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/home")
    public String home() {
        return "pages/home";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

}
