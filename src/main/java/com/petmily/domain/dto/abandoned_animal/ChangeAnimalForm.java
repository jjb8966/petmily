package com.petmily.domain.dto.abandoned_animal;

import com.petmily.domain.core.Picture;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeAnimalForm {

    private Picture picture;
    private AnimalSpecies species;
    private AnimalStatus status;
    private String name;
    private String kind;
    private Integer age;
    private Float weight;
}
