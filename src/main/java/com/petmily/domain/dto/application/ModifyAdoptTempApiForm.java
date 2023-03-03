package com.petmily.domain.dto.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ModifyAdoptTempApiForm {

    @NotBlank
    private String type;

    @NotBlank
    private String location;

    @NotBlank
    private String job;

    @NotNull
    private Boolean married;

    @NotNull
    private Integer period;

    @NotNull
    private Long animalId;

}
