package com.petmily.controller.abandoned_animal;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.dto.PetmilyPage;
import com.petmily.domain.dto.abandoned_animal.AnimalDetailForm;
import com.petmily.domain.dto_converter.AnimalDtoConverter;
import com.petmily.service.AbandonedAnimalService;
import com.petmily.service.PictureService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequestMapping("/abandoned_animal")
@RequiredArgsConstructor
@Slf4j
public class AbandonedAnimalController {

    private final AbandonedAnimalService abandonedAnimalService;
    private final PictureService pictureService;
    private final AnimalDtoConverter animalDtoConverter;

    @ResponseBody
    @GetMapping("/image/{fileStoreName}")
    public Resource getImage(@PathVariable String fileStoreName) throws MalformedURLException {
        return pictureService.findOne(fileStoreName);
    }

    @GetMapping("/list")
    public String list(@PageableDefault(size = 9, sort = "createdDate", direction = Sort.Direction.ASC) Pageable pageable,
                       Model model) {

        Page<AbandonedAnimal> allAnimal = abandonedAnimalService.findAll(pageable);
        Page<AnimalDetailForm> mapToDto = allAnimal
                .map(abandonedAnimal -> animalDtoConverter.entityToDto(abandonedAnimal, AnimalDetailForm.class)
                        .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다.")));

        PetmilyPage<AnimalDetailForm> animalPage = new PetmilyPage<>(mapToDto);

        model.addAttribute("animalPage", animalPage);

        return "/view/abandoned_animal/animal_list";
    }

    @GetMapping("/detail/{id}")
    public String detailForm(@PathVariable Long id, Model model) {
        log.info("animal id = {}", id);

        AbandonedAnimal animal = abandonedAnimalService.findOne(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유기동물입니다."));

        AnimalDetailForm animalForm = animalDtoConverter.entityToDto(animal, AnimalDetailForm.class)
                .orElseThrow(() -> new IllegalArgumentException("변환할 수 없는 폼입니다."));

        model.addAttribute("animal", animalForm);

        return "/view/abandoned_animal/detail_form";
    }
}
