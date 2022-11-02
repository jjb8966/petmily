package com.petmily.controller.member;

import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "/view/member/login_form";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        log.info("post login");

        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult.getAllErrors());

            return "/view/member/login_form";
        }

        Optional<Member> optionalMember = memberService.login(loginForm);

        if (optionalMember.isEmpty()) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "/view/member/login_form";
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("loginMember", optionalMember.get());

            return "redirect:/";
        }
    }

    @GetMapping("/join")
    public String joinForm() {
        return "/view/member/join_form";
    }
}
