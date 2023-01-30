package com.petmily.domain.dto.abandoned_animal;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ModifyAnimalApiForm {

    @NotBlank
    String species;

    @NotBlank
    String status;

    @NotBlank
    String name;

    @NotBlank
    String kind;

    @NotNull
    Integer age;

    @NotNull
    Float weight;

}
