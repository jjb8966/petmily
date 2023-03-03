package com.petmily.api.controller;

import com.petmily.api.ApiResult;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.dto.application.ApplicationListForm;
import com.petmily.domain.dto.application.DonationDetailForm;
import com.petmily.domain.dto.application.ModifyDonationApiForm;
import com.petmily.domain.dto_converter.ApplicationDtoConverter;
import com.petmily.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class DonationApiController {

    private final ApplicationService applicationService;
    private final ApplicationDtoConverter donationDtoConverter;
    private final MessageSource ms;

    @GetMapping("/donations")
    public ApiResult getList() {
        List<Application> allDonations = applicationService.findAllDonation();

        log.info("all donation {}", allDonations);

        List<ApplicationListForm> donationListForms = allDonations.stream().map(donation -> donationDtoConverter.entityToDto(donation, ApplicationListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))))
                .collect(Collectors.toList());

        ApiResult result = new ApiResult<>(donationListForms);
        result.setCount(donationListForms.size());

        return result;
    }

    @GetMapping("/donations/{donationId}")
    public DonationDetailForm donationDetail(@PathVariable Long donationId) {
        Donation donation = getDonation(donationId);
        log.info("donation = {}", donation);

        DonationDetailForm donationDetailForm = donationDtoConverter.entityToDto(donation, DonationDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));

        log.info("donation detail form = {}", donationDetailForm);

        return donationDetailForm;
    }

    private Donation getDonation(Long donationId) {
        return applicationService.findOne(donationId, Donation.class).orElseThrow(() -> new IllegalArgumentException(getMessage("exception.application.null")));
    }

    @DeleteMapping("/donations/{donationId}")
    public Map<String, String> deleteDonation(@PathVariable Long donationId) {
        applicationService.deleteApplication(donationId);

        return Map.of("message", "후원이 취소되었습니다.");
    }

    @PatchMapping("/donations/{donationId}")
    public Map<String, String> modifyDonation(@PathVariable Long donationId,
                                              @RequestBody ModifyDonationApiForm form) {

        log.info("modify member form = {}", form);

        applicationService.modifyDonation(donationId, form);

        return Map.of("message", "후원 정보가 변경되었습니다.");
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }

}
