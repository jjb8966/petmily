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

    @GetMapping("/animals")
    public String animalList() {
        return "view/admin/animal/animal_list";
    }

    @GetMapping("/animals/{animalId}")
    public String animalDetailPage(@PathVariable Long animalId, Model model) {
        model.addAttribute("animalId", animalId);

        return "view/admin/animal/animal_detail";
    }

    @GetMapping("/boards")
    public String boardList() {
        return "view/admin/board/board_list";
    }

    @GetMapping("/boards/{boardId}")
    public String boardDetailPage(@PathVariable Long boardId, Model model) {
        model.addAttribute("boardId", boardId);

        return "view/admin/board/board_detail";
    }

    @GetMapping("/donations")
    public String donationList() {
        return "view/admin/donation/donation_list";
    }

    @GetMapping("/donations/{donationId}")
    public String donationDetailPage(@PathVariable Long donationId, Model model) {
        model.addAttribute("donationId", donationId);

        return "view/admin/donation/donation_detail";
    }

    @GetMapping("/adopt_temps")
    public String adoptTempList() {
        return "view/admin/adopt_temp/adopt_temp_list";
    }

    @GetMapping("/adopt_temps/{adoptTempId}/{applicationType}")
    public String adoptTempDetailPage(@PathVariable Long adoptTempId,
                                      @PathVariable String applicationType,
                                      Model model) {

        model.addAttribute("adoptTempId", adoptTempId);
        model.addAttribute("applicationType", applicationType);

        return "view/admin/adopt_temp/adopt_temp_detail";
    }
}
