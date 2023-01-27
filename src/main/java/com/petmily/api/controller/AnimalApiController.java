package com.petmily.api.controller;

import com.petmily.api.ApiResult;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.dto.abandoned_animal.AnimalDetailForm;
import com.petmily.domain.dto.abandoned_animal.AnimalListForm;
import com.petmily.domain.dto.abandoned_animal.ModifyAnimalApiForm;
import com.petmily.domain.dto_converter.AnimalDtoConverter;
import com.petmily.service.AbandonedAnimalService;
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
public class AnimalApiController {

    private final AbandonedAnimalService animalService;
    private final AnimalDtoConverter animalDtoConverter;
    private final MessageSource ms;

    @GetMapping("/animals")
    public ApiResult getList() {
        List<AbandonedAnimal> allAnimals = animalService.findAll();

        List<AnimalListForm> animalListForms = allAnimals.stream().map(animal -> animalDtoConverter.entityToDto(animal, AnimalListForm.class)
                        .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert"))))
                .collect(Collectors.toList());

        ApiResult result = new ApiResult<>(animalListForms);
        result.setCount(animalListForms.size());

        return result;
    }

    @GetMapping("/animals/{animalId}")
    public AnimalDetailForm animalDetail(@PathVariable Long animalId) {
        AbandonedAnimal animal = getAnimal(animalId);
        log.info("animal = {}", animal);

        AnimalDetailForm animalDetailForm = animalDtoConverter.entityToDto(animal, AnimalDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException(getMessage("exception.convert")));

        log.info("animal detail form = {}", animalDetailForm);

        return animalDetailForm;
    }

    private AbandonedAnimal getAnimal(Long animalId) {
        return animalService.findOne(animalId).orElseThrow(() -> new IllegalArgumentException(getMessage("exception.animal.null")));
    }

    @DeleteMapping("/animals/{animalId}")
    public Map<String, String> deleteMember(@PathVariable Long animalId) {
        animalService.deleteAnimal(animalId);

        return Map.of("message", "유기동물이 삭제되었습니다.");
    }

    @PatchMapping("/animals/{animalId}")
    public Map<String, String> modifyAnimal(@PathVariable Long animalId,
                                            @RequestBody ModifyAnimalApiForm form) {

        log.info("modify member form = {}", form);

        animalService.modify(animalId, form);

        return Map.of("message", "유기동물의 정보가 변경되었습니다.");
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }

}
