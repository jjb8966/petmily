package com.petmily.controller.abandoned_animal;

import com.petmily.controller.SessionConstant;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.core.enum_type.BankType;
import com.petmily.domain.core.enum_type.LocationType;
import com.petmily.domain.dto.PetmilyPage;
import com.petmily.domain.dto.abandoned_animal.AnimalDetailForm;
import com.petmily.domain.dto.application.ApplyAdoptForm;
import com.petmily.domain.dto.application.ApplyDonationForm;
import com.petmily.domain.dto.application.ApplyTempProtectionForm;
import com.petmily.repository.AbandonedAnimalRepository;
import com.petmily.service.AbandonedAnimalService;
import com.petmily.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.MalformedURLException;

@Controller
@RequestMapping("/abandoned_animal")
@RequiredArgsConstructor
@Slf4j
public class AbandonedAnimalController {

    @Value("${file.dir}")
    private String storePath;

    private final AbandonedAnimalService abandonedAnimalService;
    private final AbandonedAnimalRepository abandonedAnimalRepository;
    private final ApplicationService applicationService;

    @ModelAttribute("bankType")
    public BankType[] bankTypes() {
        return BankType.values();
    }

    @ModelAttribute("locationType")
    public LocationType[] locationTypes() {
        return LocationType.values();
    }

    @ResponseBody
    @GetMapping("/image/{fileStoreName}")
    public Resource getImage(@PathVariable String fileStoreName) throws MalformedURLException {
        log.info("fileStoreName = {}", fileStoreName);
        String fullPath = "file:" + storePath + fileStoreName;
        log.info("full path = {}", fullPath);

        return new UrlResource(fullPath);
    }

    @GetMapping("/list")
    public String list(@PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable,
                       Model model) {

        Page<AbandonedAnimal> allAnimal = abandonedAnimalService.findAll(pageable);
        PetmilyPage<AnimalDetailForm> animalPage = changeToAnimalPage(allAnimal);

        model.addAttribute("animalPage", animalPage);

        return "/view/abandoned_animal/animal_list";
    }

    private PetmilyPage<AnimalDetailForm> changeToAnimalPage(Page<AbandonedAnimal> allAnimal) {
        Page<AnimalDetailForm> allAnimalForm = allAnimal.map(animal -> {
            AnimalDetailForm animalDetailForm = new AnimalDetailForm();

            animalDetailForm.setId(animal.getId());
            animalDetailForm.setPicture(animal.getPicture());
            animalDetailForm.setSpecies(animal.getSpecies());
            animalDetailForm.setStatus(animal.getStatus());
            animalDetailForm.setName(animal.getName());
            animalDetailForm.setKind(animal.getKind());
            animalDetailForm.setAge(animal.getAge());
            animalDetailForm.setWeight(animal.getWeight());

            return animalDetailForm;
        });

        return new PetmilyPage<>(allAnimalForm);
    }

    @GetMapping("/detail/{id}")
    public String detailForm(@PathVariable Long id, Model model) {
        log.info("animal id = {}", id);

        AbandonedAnimal animalOrigin = abandonedAnimalService.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        AnimalDetailForm animalForm = changeToAnimalDetailForm(animalOrigin);

        model.addAttribute("animal", animalForm);

        return "/view/abandoned_animal/detail_form";
    }

    private AnimalDetailForm changeToAnimalDetailForm(AbandonedAnimal animal) {
        AnimalDetailForm animalDetailForm = new AnimalDetailForm();

        animalDetailForm.setId(animal.getId());
        animalDetailForm.setPicture(animal.getPicture());
        animalDetailForm.setSpecies(animal.getSpecies());
        animalDetailForm.setStatus(animal.getStatus());
        animalDetailForm.setName(animal.getName());
        animalDetailForm.setKind(animal.getKind());
        animalDetailForm.setAge(animal.getAge());
        animalDetailForm.setWeight(animal.getWeight());

        return animalDetailForm;
    }

    @GetMapping("/auth/donate/{id}")
    public String donateForm(@PathVariable Long id, Model model) {

        log.info("animal id = {}", id);

        ApplyDonationForm donationDto = new ApplyDonationForm();

        model.addAttribute("animalName", abandonedAnimalRepository.findName(id));
        model.addAttribute("donationDto", donationDto);

        return "/view/abandoned_animal/donation_form";
    }

    @PostMapping("/auth/donate/{id}")
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

    @GetMapping("/auth/tempProtect/{id}")
    public String tempProtectForm(@PathVariable Long id, Model model) {

        log.info("animal id = {}", id);

        ApplyTempProtectionForm tempProtectionDto = new ApplyTempProtectionForm();

        model.addAttribute("animalName", abandonedAnimalRepository.findName(id));
        model.addAttribute("tempProtectionDto", tempProtectionDto);

        return "/view/abandoned_animal/temp_protection_form";
    }

    @PostMapping("/auth/tempProtect/{id}")
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

    @GetMapping("/auth/adopt/{id}")
    public String adoptForm(@PathVariable Long id, Model model) {

        log.info("animal id = {}", id);

        ApplyAdoptForm adoptDto = new ApplyAdoptForm();

        model.addAttribute("animalName", abandonedAnimalRepository.findName(id));
        model.addAttribute("adoptDto", adoptDto);

        return "/view/abandoned_animal/adopt_form";
    }

    @PostMapping("/auth/adopt/{id}")
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
        ;

        log.info("입양 신청 완료 {}", adopt);

        return "/view/abandoned_animal/submit_success";
    }
}
