package com.petmily.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    @GetMapping
    public String adminPage() {
        return "view/admin/admin_page";
    }

    @GetMapping("/members")
    public String memberList() {
        return "view/admin/member/member_list";
    }

    @GetMapping("/members/{memberId}")
    public String memberDetailPage(@PathVariable Long memberId, Model model) {
        model.addAttribute("memberId", memberId);

        return "view/admin/member/member_detail";
    }

    @GetMapping("/abandoned_animals")
    public String animalList() {
        return "view/admin/animal/animal_list";
    }

    @GetMapping("/boards")
    public String boardList() {
        return "view/admin/board/board_list";
    }

    @GetMapping("/adopt_temps")
    public String adoptTempList() {
        return "view/admin/adopt_temp/adopt_temp_list";
    }

    @GetMapping("/donations")
    public String donationList() {
        return "view/admin/donation_list";
    }

}
