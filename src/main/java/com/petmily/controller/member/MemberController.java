package com.petmily.controller.member;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.JoinForm;
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
import org.springframework.web.bind.annotation.RequestParam;

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
    public String login(@ModelAttribute @Validated LoginForm loginForm,
                        @RequestParam(defaultValue = "/") String redirectURL,
                        BindingResult bindingResult,
                        HttpServletRequest request) {

        log.info("form = {}", loginForm);

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
            session.setAttribute(SessionConstant.LOGIN_MEMBER, optionalMember.get());

            log.info("redirectURL = {}", redirectURL);

            return "redirect:" + redirectURL;
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("form", new JoinForm());
        return "/view/member/join_form";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("form") @Valid JoinForm form, BindingResult bindingResult) {
        log.info("form = {}", form);

        String password = form.getPassword();
        String passwordCheck = form.getPasswordCheck();

        if (!password.equals(passwordCheck)) {
            bindingResult.reject("passwordMismatch", null);
        }

        if (bindingResult.hasErrors()) {
            log.info("회원가입 실패 {}", bindingResult.getAllErrors());
            return "/view/member/join_form";
        }

        Member member = new MemberBuilder(form.getLoginId(), form.getPassword())
                .setName(form.getName())
                .setBirth(form.getBirth())
                .setEmail(form.getEmail())
                .setPhone(form.getPhone()).build();

        memberService.join(member);

        log.info("회원가입 완료 member = {}", member);

        return "redirect:/login";
    }
}
