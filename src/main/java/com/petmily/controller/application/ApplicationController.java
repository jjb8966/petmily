package com.petmily.controller.application;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.core.enum_type.BankType;
import com.petmily.domain.core.enum_type.LocationType;
import com.petmily.domain.dto.application.*;
import com.petmily.domain.dto_converter.ApplicationDtoConverter;
import com.petmily.repository.AbandonedAnimalRepository;
import com.petmily.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ApplicationController {

    private final ApplicationService applicationService;
    private final AbandonedAnimalRepository abandonedAnimalRepository;
    private final ApplicationDtoConverter applicationDtoConverter;

    @ModelAttribute("bankType")
    public BankType[] bankTypes() {
        return BankType.values();
    }

    @ModelAttribute("locationType")
    public LocationType[] locationTypes() {
        return LocationType.values();
    }

    @GetMapping("/abandoned_animal/auth/donate/{id}")
    public String donateForm(@PathVariable Long id, Model model) {

        log.info("animal id = {}", id);

        ApplyDonationForm donationDto = new ApplyDonationForm();

        model.addAttribute("animalName", abandonedAnimalRepository.findName(id));
        model.addAttribute("donationDto", donationDto);

        return "/view/abandoned_animal/donation_form";
    }

    @PostMapping("/abandoned_animal/auth/donate/{id}")
    public String donate(@PathVariable("id") Long animalId,
                         @SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                         @ModelAttribute("donationDto") @Valid ApplyDonationForm donationDto,
                         BindingResult bindingResult,
                         Model model) {

        log.info("donation info = {}", donationDto);

        if (bindingResult.hasErrors()) {
            log.info("error {}", bindingResult.getAllErrors());
            model.addAttribute("animalName", abandonedAnimalRepository.findName(animalId));

            return "/view/abandoned_animal/donation_form";
        }

        Long donateId = applicationService.donate(loginMember.getId(), animalId, donationDto);
        Donation donation = applicationService.findOne(donateId, Donation.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        log.info("후원 신청 완료 {}", donation);

        return "/view/abandoned_animal/submit_success";
    }

    @GetMapping("/abandoned_animal/auth/tempProtect/{id}")
    public String tempProtectForm(@PathVariable Long id, Model model) {

        log.info("animal id = {}", id);

        ApplyTempProtectionForm tempProtectionDto = new ApplyTempProtectionForm();

        model.addAttribute("animalName", abandonedAnimalRepository.findName(id));
        model.addAttribute("tempProtectionDto", tempProtectionDto);

        return "/view/abandoned_animal/temp_protection_form";
    }

    @PostMapping("/abandoned_animal/auth/tempProtect/{id}")
    public String tempProtect(@PathVariable("id") Long animalId,
                              @SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                              @ModelAttribute("tempProtectionDto") @Valid ApplyTempProtectionForm tempProtectionDto,
                              BindingResult bindingResult,
                              Model model) {

        log.info("tempProtection info = {}", tempProtectionDto);

        if (bindingResult.hasErrors()) {
            log.info("error {}", bindingResult.getAllErrors());
            model.addAttribute("animalName", abandonedAnimalRepository.findName(animalId));

            return "/view/abandoned_animal/temp_protection_form";
        }

        Long tempProtectId = applicationService.tempProtect(loginMember.getId(), animalId, tempProtectionDto);
        TemporaryProtection temporaryProtection = applicationService.findOne(tempProtectId, TemporaryProtection.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        log.info("임시보호 신청 완료 {}", temporaryProtection);

        return "/view/abandoned_animal/submit_success";
    }

    @GetMapping("/abandoned_animal/auth/adopt/{id}")
    public String adoptForm(@PathVariable Long id, Model model) {

        log.info("animal id = {}", id);

        ApplyAdoptForm adoptDto = new ApplyAdoptForm();

        model.addAttribute("animalName", abandonedAnimalRepository.findName(id));
        model.addAttribute("adoptDto", adoptDto);

        return "/view/abandoned_animal/adopt_form";
    }

    @PostMapping("/abandoned_animal/auth/adopt/{id}")
    public String adopt(@PathVariable("id") Long animalId,
                        @SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                        @ModelAttribute("adoptDto") @Valid ApplyAdoptForm adoptDto,
                        BindingResult bindingResult,
                        Model model) {

        log.info("adopt info = {}", adoptDto);

        if (bindingResult.hasErrors()) {
            log.info("error {}", bindingResult.getAllErrors());
            model.addAttribute("animalName", abandonedAnimalRepository.findName(animalId));

            return "/view/abandoned_animal/adopt_form";
        }

        Long adoptId = applicationService.adopt(loginMember.getId(), animalId, adoptDto);
        Adopt adopt = applicationService.findOne(adoptId, Adopt.class)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지원서입니다."));

        log.info("입양 신청 완료 {}", adopt);

        return "/view/abandoned_animal/submit_success";
    }

    @GetMapping("/member/auth/application/list")
    public String applicationList(@SessionAttribute(name = SessionConstant.LOGIN_MEMBER) Member loginMember,
                                  Model model) {

        List<Application> applications = applicationService.findAll(loginMember);

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

}
