package com.petmily.api.controller;

import com.petmily.api.ApiResult;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.dto.application.*;
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
public class AdoptTempApiController {

    private final ApplicationService applicationService;
    private final ApplicationDtoConverter applicationDtoConverter;
    private final MessageSource ms;

    @GetMapping("/adopt_temps")
    public ApiResult getList() {
        List<Application> allAdoptTemps = applicationService.findAllAdoptTemp();

        log.info("all adoptTemp {}", allAdoptTemps);

        List<ApplicationListForm> adoptTempListForms = allAdoptTemps.stream()
                .map(adoptTemp -> applicationDtoConverter.entityToDto(adoptTemp, ApplicationListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))))
                .collect(Collectors.toList());

        ApiResult result = new ApiResult<>(adoptTempListForms);
        result.setCount(adoptTempListForms.size());

        return result;
    }

    @GetMapping("/adopt_temps/{adoptTempId}/{applicationType}")
    public AdoptTempDetailForm adoptTempDetail(@PathVariable Long adoptTempId, @PathVariable String applicationType) {
        Application adoptOrTemp = null;
        AdoptTempDetailForm adoptTempDetailForm = null;

        if (applicationType.equals("Adopt")) {
            adoptOrTemp = getApplication(adoptTempId, Adopt.class);
            adoptTempDetailForm = applicationDtoConverter.entityToDto(adoptOrTemp, AdoptDetailForm.class)
                    .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));
        }

        if (applicationType.equals("TemporaryProtection")) {
            adoptOrTemp = getApplication(adoptTempId, TemporaryProtection.class);
            adoptTempDetailForm = applicationDtoConverter.entityToDto(adoptOrTemp, TempProtectionDetailForm.class)
                    .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));
        }

        log.info("adoptTemp = {}", adoptOrTemp);
        log.info("adoptTemp detail form = {}", adoptTempDetailForm);

        return adoptTempDetailForm;
    }

    private <T extends Application> Application getApplication(Long adoptTempId, Class<T> adoptOrTemp) {
        return applicationService.findOne(adoptTempId, adoptOrTemp)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.application.null")));
    }

    @DeleteMapping("/adopt_temps/{adoptTempId}/{applicationType}")
    public Map<String, String> deleteAdoptTemp(@PathVariable Long adoptTempId, @PathVariable String applicationType) {
        String message = "";

        applicationService.deleteApplication(adoptTempId);

        if (applicationType.equals("Adopt")) {
            message = "입양 신청이 취소되었습니다.";
        }

        if (applicationType.equals("TemporaryProtection")) {
            message = "임시보호 신청이 취소되었습니다.";
        }

        return Map.of("message", message);
    }

    @PatchMapping("/adopt_temps/{adoptTempId}/{applicationType}")
    public Map<String, String> modifyAdoptTemp(@PathVariable Long adoptTempId,
                                               @PathVariable String applicationType,
                                               @RequestBody ModifyAdoptTempApiForm form) {
        String message = "";

        log.info("modify adopt_temp form = {}", form);

        applicationService.modifyAdoptTemp(adoptTempId, form);

        if (applicationType.equals("Adopt")) {
            message = "입양 정보가 변경되었습니다.";
        }

        if (applicationType.equals("TemporaryProtection")) {
            message = "임시보호 정보가 변경되었습니다.";
        }

        return Map.of("message", message);
    }

    @PostMapping("/adopt_temps/approve/{adoptTempId}")
    public Map<String, String> approveApplication(@PathVariable Long adoptTempId) {
        applicationService.approveApplication(adoptTempId);

        return Map.of("message", "승인되었습니다.");
    }

    @PostMapping("/adopt_temps/refuse/{adoptTempId}")
    public Map<String, String> refuseApplication(@PathVariable Long adoptTempId) {
        applicationService.refuseApplication(adoptTempId);

        return Map.of("message", "거절되었습니다.");
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }

}
