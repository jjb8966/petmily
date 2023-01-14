package com.petmily.domain.dto.abandoned_animal;

import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AnimalListForm {

    private Long id;
    private String name;
    private AnimalSpecies species;
    private AnimalStatus status;

}
