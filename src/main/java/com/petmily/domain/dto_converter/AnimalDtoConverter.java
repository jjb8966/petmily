package com.petmily.domain.dto_converter;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.BaseEntity;
import com.petmily.domain.dto.abandoned_animal.AnimalDetailForm;
import com.petmily.domain.dto.abandoned_animal.ChangeAnimalForm;
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

        if (dtoType.isAssignableFrom(AnimalDetailForm.class)) {
            log.info("AnimalDetailForm 변환");
            dto = (T) convertToAnimalDetailForm(animal);
        }

        if (dtoType.isAssignableFrom(ChangeAnimalForm.class)) {
            log.info("ChangeAnimalForm 변환");
            dto = (T) convertToChangeAnimalForm(animal);
        }

        return Optional.ofNullable(dto);
    }

    private ChangeAnimalForm convertToChangeAnimalForm(AbandonedAnimal animal) {
        ChangeAnimalForm changeAnimalForm = new ChangeAnimalForm();

        changeAnimalForm.setSpecies(animal.getSpecies());
        changeAnimalForm.setName(animal.getName());
        changeAnimalForm.setKind(animal.getKind());
        changeAnimalForm.setAge(animal.getAge());
        changeAnimalForm.setWeight(animal.getWeight());

        return changeAnimalForm;
    }

    private AnimalDetailForm convertToAnimalDetailForm(AbandonedAnimal animal) {
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
}
