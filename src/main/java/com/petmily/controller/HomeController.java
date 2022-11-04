package com.petmily.controller;

import com.petmily.domain.core.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER, required = false) Member loginMember,
                       Model model) {

        if (loginMember != null) {
            model.addAttribute(SessionConstant.LOGIN_MEMBER, loginMember);
        }

        return "index";
    }
}
