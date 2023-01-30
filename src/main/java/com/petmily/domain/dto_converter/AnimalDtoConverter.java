package com.petmily.domain.dto_converter;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.dto.abandoned_animal.AnimalDetailForm;
import com.petmily.domain.dto.abandoned_animal.AnimalListForm;
import com.petmily.domain.dto.abandoned_animal.ModifyAnimalForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class AnimalDtoConverter implements EntityDtoConverter {

    @Override
    public <T> Optional<T> entityToDto(BaseEntity entity, Class<T> dtoType) {
        AbandonedAnimal animal = (AbandonedAnimal) entity;
        T dto = null;

        if (AnimalDetailForm.class.isAssignableFrom(dtoType)) {
            log.info("AnimalDetailForm 변환");
            dto = (T) convertToAnimalDetailForm(animal);
        }

        if (ModifyAnimalForm.class.isAssignableFrom(dtoType)) {
            log.info("ModifyAnimalForm 변환");
            dto = (T) convertToChangeAnimalForm(animal);
        }

        if (AnimalListForm.class.isAssignableFrom(dtoType)) {
            dto = (T) convertToAnimalListForm(animal);
        }

        return Optional.ofNullable(dto);
    }

    private ModifyAnimalForm convertToChangeAnimalForm(AbandonedAnimal animal) {
        ModifyAnimalForm modifyAnimalForm = new ModifyAnimalForm();

        modifyAnimalForm.setSpecies(animal.getSpecies());
        modifyAnimalForm.setName(animal.getName());
        modifyAnimalForm.setKind(animal.getKind());
        modifyAnimalForm.setAge(animal.getAge());
        modifyAnimalForm.setWeight(animal.getWeight());

        return modifyAnimalForm;
    }

    private AnimalDetailForm convertToAnimalDetailForm(AbandonedAnimal animal) {
        AnimalDetailForm animalDetailForm = new AnimalDetailForm();

        animalDetailForm.setId(animal.getId());
        animalDetailForm.setPictureStoreName(animal.getPicture().getFileStoreName());
        animalDetailForm.setSpecies(animal.getSpecies());
        animalDetailForm.setStatus(animal.getStatus());
        animalDetailForm.setName(animal.getName());
        animalDetailForm.setKind(animal.getKind());
        animalDetailForm.setAge(animal.getAge());
        animalDetailForm.setWeight(animal.getWeight());

        return animalDetailForm;
    }

    private AnimalListForm convertToAnimalListForm(AbandonedAnimal animal) {
        AnimalListForm animalListForm = new AnimalListForm();

        animalListForm.setId(animal.getId());
        animalListForm.setName(animal.getName());
        animalListForm.setSpecies(animal.getSpecies());
        animalListForm.setStatus(animal.getStatus());

        return animalListForm;
    }

}
