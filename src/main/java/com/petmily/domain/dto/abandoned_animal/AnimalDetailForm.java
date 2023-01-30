package com.petmily.domain.dto.abandoned_animal;

import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnimalDetailForm {

    private Long id;
    private String pictureStoreName;
    private AnimalSpecies species;
    private AnimalStatus status;
    private String name;
    private String kind;
    private Integer age;
    private Float weight;

}
