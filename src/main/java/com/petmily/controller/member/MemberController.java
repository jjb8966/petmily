package com.petmily.controller.member;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.dto.application.AdoptDetailForm;
import com.petmily.domain.dto.application.ApplicationListForm;
import com.petmily.domain.dto.application.DonationDetailForm;
import com.petmily.domain.dto.application.TempProtectionDetailForm;
import com.petmily.domain.dto.member.JoinForm;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.domain.dto.member.ModifyMemberForm;
import com.petmily.domain.dto.member.WithdrawMemberForm;
import com.petmily.service.AbandonedAnimalService;
import com.petmily.service.ApplicationService;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final ApplicationService applicationService;
    private final AbandonedAnimalService animalService;

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

    private static Member changeToMember(JoinForm form) {
        return new MemberBuilder(form.getLoginId(), form.getPassword())
                .setName(form.getName())
                .setBirth(form.getBirth())
                .setEmail(form.getEmail())
                .setPhone(form.getPhone())
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

        ModifyMemberForm modifyMemberForm = changeToModifyForm(member);

        model.addAttribute("modifyMemberForm", modifyMemberForm);

        return "/view/member/modify_form";
    }

    private ModifyMemberForm changeToModifyForm(Member member) {
        ModifyMemberForm modifyMemberForm = new ModifyMemberForm();

        modifyMemberForm.setLoginId(member.getLoginId());
        modifyMemberForm.setPassword(member.getPassword());
        modifyMemberForm.setName(member.getName());
        modifyMemberForm.setPhone(member.getPhone());
        modifyMemberForm.setEmail(member.getEmail());

        return modifyMemberForm;
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

    @GetMapping("/member/auth/apply/{memberId}")
    public String applyList(@PathVariable Long memberId, Model model) {
        log.info("memberId = {}", memberId);

        Member member = memberService.findOne(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Application> applications = applicationService.findAll(member);
        List<ApplicationListForm> forms = changeToApplicationListForm(applications);

        model.addAttribute("forms", forms);

        return "/view/member/apply_list";
    }

    private List<ApplicationListForm> changeToApplicationListForm(List<Application> applications) {
        return applications.stream()
                .map(application -> {
                    ApplicationListForm form = new ApplicationListForm();

                    form.setId(application.getId());
                    form.setAnimalName(application.getAbandonedAnimal().getName());
                    form.setType(application.getApplicationType());
                    form.setStatus(application.getApplicationStatus());

                    return form;
                })
                .collect(Collectors.toList());
    }

    private String getType(String applicationType) {
        String result = null;

        if (applicationType.equals("Donation")) {
            result = "후원";
        }

        if (applicationType.equals("TemporaryProtection")) {
            result = "임시보호";
        }

        if (applicationType.equals("Adopt")) {
            result = "입양";
        }

        return result;
    }

    @GetMapping("/member/auth/apply/detail/{appType}/{appId}")
    public String applicationDetailForm(@PathVariable String appType,
                                        @PathVariable Long appId,
                                        Model model) {

        log.info("id = {}", appId);

        if (appType.equals("Donation")) {
            Donation donation = applicationService.findOne(appId, Donation.class)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

            DonationDetailForm form = changeToDonationDetailForm(donation);
            model.addAttribute("form", form);
        }

        if (appType.equals("TemporaryProtection")) {
            TemporaryProtection temporaryProtection = applicationService.findOne(appId, TemporaryProtection.class)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

            TempProtectionDetailForm form = changeToTempProtectionDetailForm(temporaryProtection);
            model.addAttribute("form", form);
        }

        if (appType.equals("Adopt")) {
            Adopt adopt = applicationService.findOne(appId, Adopt.class)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

            AdoptDetailForm form = changeToAdoptDetailForm(adopt);
            model.addAttribute("form", form);
        }

        model.addAttribute("appType", appType);

        return "/view/member/apply_detail_form";
    }

    private TempProtectionDetailForm changeToTempProtectionDetailForm(TemporaryProtection temporaryProtection) {
        AbandonedAnimal animal = animalService.findOne(temporaryProtection.getAbandonedAnimal().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        TempProtectionDetailForm form = new TempProtectionDetailForm();

        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());
        form.setLocation(temporaryProtection.getLocation());
        form.setJob(temporaryProtection.getJob());
        form.setMarried(temporaryProtection.getMarried());
        form.setPeriod(temporaryProtection.getPeriod());

        return form;
    }

    private AdoptDetailForm changeToAdoptDetailForm(Adopt adopt) {
        AbandonedAnimal animal = animalService.findOne(adopt.getAbandonedAnimal().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        AdoptDetailForm form = new AdoptDetailForm();

        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());
        form.setLocation(adopt.getLocation());
        form.setJob(adopt.getJob());
        form.setMarried(adopt.getMarried());

        return form;
    }

    private DonationDetailForm changeToDonationDetailForm(Donation donation) {
        AbandonedAnimal animal = animalService.findOne(donation.getAbandonedAnimal().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        DonationDetailForm form = new DonationDetailForm();

        form.setFileStoreName(animal.getPicture().getFileStoreName());
        form.setStatus(animal.getStatus());
        form.setSpecies(animal.getSpecies());
        form.setKind(animal.getKind());
        form.setAnimalName(animal.getName());
        form.setAge(animal.getAge());
        form.setWeight(animal.getWeight());

        form.setBankType(donation.getBankType());
        form.setDonator(donation.getDonator());
        form.setAccountNumber(donation.getAccountNumber());
        form.setAmount(donation.getAmount());

        return form;
    }

}
