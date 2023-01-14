package com.petmily.api.controller;

import com.petmily.api.ApiResult;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.dto.abandoned_animal.AnimalListForm;
import com.petmily.domain.dto_converter.AnimalDtoConverter;
import com.petmily.service.AbandonedAnimalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
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

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }

}
