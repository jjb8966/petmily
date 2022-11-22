package com.petmily.controller.member;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.domain.dto.member.MemberJoinForm;
import com.petmily.domain.dto.member.ModifyMemberForm;
import com.petmily.domain.dto.member.WithdrawMemberForm;
import com.petmily.domain.dto_converter.MemberDtoConverter;
import com.petmily.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberDtoConverter memberDtoConverter;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginForm", new LoginForm());
        return "/view/member/login_form";
    }

    @PostMapping("/login")
    public String login(@RequestParam(defaultValue = "/") String redirectURL,
                        @ModelAttribute @Validated LoginForm loginForm,
                        BindingResult bindingResult,
                        HttpSession session) {

        log.info("form = {}", loginForm);

        if (bindingResult.hasErrors()) {
            log.info("error = {}", bindingResult.getAllErrors());

            return "/view/member/login_form";
        }

        Optional<Member> loginMember = memberService.login(loginForm);

        if (loginMember.isEmpty()) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "/view/member/login_form";
        }

        session.setAttribute(SessionConstant.LOGIN_MEMBER, loginMember.get());

        log.info("redirectURL = {}", redirectURL);

        return "redirect:" + redirectURL;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null && session.getAttribute(SessionConstant.LOGIN_MEMBER) != null) {
            session.invalidate();
        }

        return "redirect:/";
    }

    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("form", new MemberJoinForm());
        return "/view/member/join_form";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute("form") @Valid MemberJoinForm form, BindingResult bindingResult) {
        log.info("form = {}", form);

        String password = form.getPassword();
        String passwordCheck = form.getPasswordCheck();

        if (hasText(password, passwordCheck) && !matchPasswordCheck(password, passwordCheck)) {
            bindingResult.reject("passwordMismatch", null);
        }

        if (bindingResult.hasErrors()) {
            log.info("회원가입 실패 {}", bindingResult.getAllErrors());
            return "/view/member/join_form";
        }

        Member member = changeToMember(form);

        memberService.join(member);

        log.info("회원가입 완료 member = {}", member);

        return "redirect:/login";
    }

    private boolean hasText(String password, String passwordCheck) {
        return StringUtils.hasText(password) && StringUtils.hasText(passwordCheck);
    }

    private boolean matchPasswordCheck(String password, String passwordCheck) {
        return password.equals(passwordCheck);
    }

    private static Member changeToMember(MemberJoinForm form) {
        return new MemberBuilder(form.getLoginId(), form.getPassword())
                .setName(form.getName())
                .setBirth(form.getBirth())
                .setEmail(form.getEmail())
                .setPhoneNumber(form.getPhoneNumber())
                .build();
    }

    @GetMapping("/member/auth/mypage")
    public String mypage() {
        return "/view/member/mypage";
    }

    @GetMapping("/member/auth/modify")
    public String modifyForm(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                             Model model) {

        Member member = memberService.findOne(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        ModifyMemberForm modifyMemberForm = memberDtoConverter.entityToDto(member, ModifyMemberForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));

        model.addAttribute("modifyMemberForm", modifyMemberForm);

        return "/view/member/modify_form";
    }

    @PostMapping("/member/auth/modify")
    public String modify(@ModelAttribute @Valid ModifyMemberForm form,
                         BindingResult bindingResult,
                         HttpSession session) {

        if (bindingResult.hasErrors()) {
            log.info("회원정보 변경 실패 {}", bindingResult.getAllErrors());
            return "/view/member/modify_form";
        }

        Member loginMember = (Member) session.getAttribute(SessionConstant.LOGIN_MEMBER);

        Long memberId = memberService.modify(loginMember.getId(), form);
        Member modifiedMember = memberService.findOne(memberId).orElseThrow();

        session.setAttribute(SessionConstant.LOGIN_MEMBER, modifiedMember);

        log.info("회원정보 변경 성공 {}", modifiedMember);

        return "redirect:/member/auth/mypage";
    }

    @GetMapping("/member/auth/withdraw")
    public String withdrawForm(Model model) {
        model.addAttribute("withdrawMemberForm", new WithdrawMemberForm());
        return "/view/member/withdraw_form";
    }

    @PostMapping("/member/auth/withdraw")
    public String withdraw(@ModelAttribute @Valid WithdrawMemberForm form,
                           BindingResult bindingResult,
                           HttpSession session) {

        Member loginMember = (Member) session.getAttribute(SessionConstant.LOGIN_MEMBER);

        String password = form.getPassword();
        String passwordCheck = form.getPasswordCheck();

        if (hasText(password, passwordCheck) && !matchPasswordCheck(password, passwordCheck)) {
            bindingResult.reject("passwordMismatch", null);
        }

        if (hasText(password, passwordCheck) && matchPasswordCheck(password, passwordCheck) && !correctPassword(password, loginMember.getPassword())) {
            bindingResult.reject("incorrectPassword", null);
        }

        if (bindingResult.hasErrors()) {
            log.info("회원 탈퇴 실패 {}", bindingResult.getAllErrors());
            return "/view/member/withdraw_form";
        }

        memberService.withdrawMember(loginMember.getId());
        session.invalidate();

        log.info("회원 탈퇴 성공");

        return "redirect:/";
    }

    private boolean correctPassword(String password, String loginMemberPassword) {
        return password.equals(loginMemberPassword);
    }

}
