package com.petmily.domain.dto.application;

import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import com.petmily.domain.enum_type.LocationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class TempProtectionDetailForm implements AdoptTempDetailForm {

    // 유기동물 정보
    private Long animalId;
    private String fileStoreName;
    private AnimalStatus status;
    private AnimalSpecies species;
    private String kind;
    private String animalName;
    private Integer age;
    private Float weight;

    private Long applicationId;

    @NotBlank
    private String applicantName;

    @NotNull
    private LocationType location;

    @NotBlank
    private String job;

    @NotNull
    private Boolean married;

    @NotNull
    private Integer period;
}
