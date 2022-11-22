package com.petmily.controller.member;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.core.enum_type.BankType;
import com.petmily.domain.core.enum_type.LocationType;
import com.petmily.domain.dto.application.*;
import com.petmily.domain.dto.board.BoardListForm;
import com.petmily.domain.dto.member.LoginForm;
import com.petmily.domain.dto.member.MemberJoinForm;
import com.petmily.domain.dto.member.ModifyMemberForm;
import com.petmily.domain.dto.member.WithdrawMemberForm;
import com.petmily.domain.dto_converter.ApplicationDtoConverter;
import com.petmily.domain.dto_converter.BoardDtoConverter;
import com.petmily.domain.dto_converter.MemberDtoConverter;
import com.petmily.service.ApplicationService;
import com.petmily.service.BoardService;
import com.petmily.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private final BoardService boardService;
    private final ApplicationDtoConverter applicationDtoConverter;
    private final MemberDtoConverter memberDtoConverter;
    private final BoardDtoConverter boardDtoConverter;

    @ModelAttribute(name = "bankType")
    public BankType[] bankTypes() {
        return BankType.values();
    }

    @ModelAttribute(name = "locationType")
    public LocationType[] locationTypes() {
        return LocationType.values();
    }

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

    @GetMapping("/member/auth/application/list")
    public String applicationList(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                                  Model model) {

        Member member = memberService.findOne(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Application> applications = applicationService.findAll(member);

        List<ApplicationListForm> forms = applications.stream()
                .map(application -> applicationDtoConverter.entityToDto(application, ApplicationListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다.")))
                .collect(Collectors.toList());

        model.addAttribute("forms", forms);

        return "/view/member/application_list";
    }

    @GetMapping("/member/auth/application/detail/{appType}/{appId}")
    public String applicationDetailForm(@PathVariable String appType,
                                        @PathVariable Long appId,
                                        Model model) {

        log.info("id = {}", appId);

        if (appType.equals("Donation")) {
            DonationDetailForm form = getDonationDetailForm(appId);
            model.addAttribute("form", form);
        }

        if (appType.equals("TemporaryProtection")) {
            TempProtectionDetailForm form = getTempProtectionDetailForm(appId);
            model.addAttribute("form", form);
        }

        if (appType.equals("Adopt")) {
            AdoptDetailForm form = getAdoptDetailForm(appId);
            model.addAttribute("form", form);
        }

        model.addAttribute("appType", appType);

        return "/view/member/application_detail_form";
    }

    private DonationDetailForm getDonationDetailForm(Long appId) {
        Donation donation = applicationService.findOne(appId, Donation.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

        return applicationDtoConverter.entityToDto(donation, DonationDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));
    }

    private TempProtectionDetailForm getTempProtectionDetailForm(Long appId) {
        TemporaryProtection temporaryProtection = applicationService.findOne(appId, TemporaryProtection.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

        return applicationDtoConverter.entityToDto(temporaryProtection, TempProtectionDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));
    }

    private AdoptDetailForm getAdoptDetailForm(Long appId) {
        Adopt adopt = applicationService.findOne(appId, Adopt.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 신청서입니다."));

        return applicationDtoConverter.entityToDto(adopt, AdoptDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));
    }

    @GetMapping("/member/auth/application/modify/{appType}/{appId}")
    public String modifyApplicationForm(@PathVariable String appType,
                                        @PathVariable Long appId,
                                        Model model) {

        if (appType.equals("Donation")) {
            DonationDetailForm form = getDonationDetailForm(appId);
            model.addAttribute("form", form);
            log.info("form = {}", form);
        }

        if (appType.equals("TemporaryProtection")) {
            TempProtectionDetailForm form = getTempProtectionDetailForm(appId);
            model.addAttribute("form", form);
            log.info("form = {}", form);
        }

        if (appType.equals("Adopt")) {
            AdoptDetailForm form = getAdoptDetailForm(appId);
            model.addAttribute("form", form);
            log.info("form = {}", form);
        }

        return "/view/member/application_modify_form";
    }

    @PostMapping("/member/auth/application/modify/Donation/{appId}")
    public String modifyDonation(@PathVariable Long appId,
                                 @ModelAttribute("form") @Valid DonationDetailForm form,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {

        log.info("form = {}", form);

        redirectAttributes.addAttribute("appId", appId);

        if (bindingResult.hasErrors()) {
            log.error("error {}", bindingResult.getAllErrors());
            model.addAttribute("appType", "Donation");

            return "/view/member/application_modify_form";
        }

        ModifyDonationForm modifyForm = changeToModifyDonationForm(form);
        applicationService.modifyDonation(appId, modifyForm);

        return "redirect:/member/auth/application/detail/Donation/{appId}";
    }

    private ModifyDonationForm changeToModifyDonationForm(DonationDetailForm form) {
        ModifyDonationForm modifyForm = new ModifyDonationForm();

        modifyForm.setDonator(form.getDonator());
        modifyForm.setBankType(form.getBankType());
        modifyForm.setAccountNumber(form.getAccountNumber());
        modifyForm.setAmount(form.getAmount());

        return modifyForm;
    }

    @PostMapping("/member/auth/application/modify/TemporaryProtection/{appId}")
    public String modifyTempProtection(@PathVariable Long appId,
                                       @ModelAttribute("form") @Valid TempProtectionDetailForm form,
                                       BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes,
                                       Model model) {

        log.info("form = {}", form);

        redirectAttributes.addAttribute("appId", appId);

        if (bindingResult.hasErrors()) {
            log.error("error {}", bindingResult.getAllErrors());
            model.addAttribute("appType", "TemporaryProtection");

            return "/view/member/application_modify_form";
        }

        ModifyTempProtectionForm modifyForm = changeToModifyTempProtectionForm(form);
        applicationService.modifyTempProtection(appId, modifyForm);

        return "redirect:/member/auth/application/detail/TemporaryProtection/{appId}";
    }

    private ModifyTempProtectionForm changeToModifyTempProtectionForm(TempProtectionDetailForm form) {
        ModifyTempProtectionForm modifyForm = new ModifyTempProtectionForm();

        modifyForm.setLocation(form.getLocation());
        modifyForm.setJob(form.getJob());
        modifyForm.setMarried(form.getMarried());
        modifyForm.setPeriod(form.getPeriod());

        return modifyForm;
    }

    @PostMapping("/member/auth/application/modify/Adopt/{appId}")
    public String modifyAdopt(@PathVariable Long appId,
                              @ModelAttribute("form") @Valid AdoptDetailForm form,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              Model model) {

        log.info("form = {}", form);

        redirectAttributes.addAttribute("appId", appId);

        if (bindingResult.hasErrors()) {
            log.error("error {}", bindingResult.getAllErrors());
            model.addAttribute("appType", "Adopt");

            return "/view/member/application_modify_form";
        }

        ModifyAdoptForm modifyForm = changeToModifyAdoptForm(form);
        applicationService.modifyAdopt(appId, modifyForm);

        return "redirect:/member/auth/application/detail/Adopt/{appId}";
    }

    private ModifyAdoptForm changeToModifyAdoptForm(AdoptDetailForm form) {
        ModifyAdoptForm modifyForm = new ModifyAdoptForm();

        modifyForm.setLocation(form.getLocation());
        modifyForm.setJob(form.getJob());
        modifyForm.setMarried(form.getMarried());

        return modifyForm;
    }

    @PostMapping("/member/auth/application/delete/{appId}")
    public String deleteApplication(@PathVariable Long appId) {
        applicationService.deleteApplication(appId);

        return "redirect:/member/auth/application/list";
    }

    @GetMapping("/member/auth/board/list")
    public String boardList(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                            Model model) {

        Member member = memberService.findOne(loginMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<Board> boards = boardService.findAll(member);

        List<BoardListForm> forms = boards.stream()
                .map(board -> boardDtoConverter.entityToDto(board, BoardListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다.")))
                .collect(Collectors.toList());

        model.addAttribute("forms", forms);

        return "/view/member/board_list";
    }

}
