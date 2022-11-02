package com.petmily.controller;

import com.petmily.domain.core.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginMember", required = false) Member loginMember,
                       Model model) {

        if (loginMember != null) {
            model.addAttribute("loginMember", loginMember);
        }

        return "index";
    }
}
