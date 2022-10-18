package com.petmily;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("/hi")
    public String hi(Model model) {
        model.addAttribute("data", "hihihihi");

        return "hello";
    }
}
