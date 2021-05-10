package com.axmor.axmortest.controller;

import com.axmor.axmortest.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String getLogin(@AuthenticationPrincipal User user) {
        if (user != null) {
            return "redirect:/";
        }
        return "login";
    }
}
