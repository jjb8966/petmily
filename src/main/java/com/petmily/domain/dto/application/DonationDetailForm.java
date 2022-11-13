package com.petmily.domain.dto.application;

import com.petmily.domain.core.enum_type.AnimalSpecies;
import com.petmily.domain.core.enum_type.AnimalStatus;
import com.petmily.domain.core.enum_type.BankType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class DonationDetailForm {

    // 유기동물 정보
    private String fileStoreName;
    private AnimalStatus status;
    private AnimalSpecies species;
    private String kind;
    private String animalName;
    private Integer age;
    private Float weight;

    private Long applicationId;

    @NotNull
    private BankType bankType;

    @NotBlank
    private String donator;

    @NotBlank
    private String accountNumber;

    @NotNull
    private Integer amount;
}