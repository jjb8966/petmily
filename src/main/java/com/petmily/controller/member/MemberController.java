package com.petmily.controller.member;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.core.Member;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.domain.dto.member.MemberJoinForm;
import com.petmily.domain.dto.member.ModifyMemberForm;
import com.petmily.domain.dto.member.WithdrawMemberForm;
import com.petmily.domain.dto_converter.MemberDtoConverter;
import com.petmily.exception.DuplicateLoginIdException;
import com.petmily.exception.PasswordIncorrectException;
import com.petmily.exception.PasswordMismatchException;
import com.petmily.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Locale;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final MemberDtoConverter memberDtoConverter;
    private final MessageSource ms;

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
            bindingResult.reject("loginFail");
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

        if (bindingResult.hasErrors()) {
            log.info("회원가입 실패 {}", bindingResult.getAllErrors());
            return "/view/member/join_form";
        }

        try {
            memberService.join(form);
        } catch (PasswordMismatchException e) {
            log.info("PasswordMismatchException 발생", e);
            bindingResult.reject("passwordMismatch");

            return "/view/member/join_form";
        } catch (DuplicateLoginIdException e) {
            log.info("DuplicateLoginIdException 발생", e);
            bindingResult.reject("duplicateLoginId");

            return "/view/member/join_form";
        }

        return "redirect:/login";
    }

    @GetMapping("/member/auth/mypage")
    public String mypage() {
        return "/view/member/mypage";
    }

    @GetMapping("/member/auth/modify")
    public String modifyForm(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                             Model model) {

        Member member = memberService.findOne(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.member.null")));

        ModifyMemberForm modifyMemberForm = memberDtoConverter.entityToDto(member, ModifyMemberForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));

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

        log.info("form = {}", form);

        Member loginMember = (Member) session.getAttribute(SessionConstant.LOGIN_MEMBER);

        if (bindingResult.hasErrors()) {
            log.info("회원 탈퇴 실패 {}", bindingResult.getAllErrors());
            return "/view/member/withdraw_form";
        }

        try {
            memberService.withdrawMember(loginMember.getId(), loginMember.getPassword(), form);
            session.invalidate();
        } catch (PasswordMismatchException e) {
            log.info("PasswordMismatchException 발생", e);
            bindingResult.reject("passwordMismatch");

            return "/view/member/withdraw_form";
        } catch (PasswordIncorrectException e) {
            log.info("PasswordIncorrectException 발생", e);
            bindingResult.reject("incorrectPassword");

            return "/view/member/withdraw_form";
        }

        log.info("회원 탈퇴 성공");

        return "redirect:/";
    }

    private boolean correctPassword(String password, String loginMemberPassword) {
        return password.equals(loginMemberPassword);
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}
