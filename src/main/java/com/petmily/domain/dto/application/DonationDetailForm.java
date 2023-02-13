package com.petmily.domain.dto.application;

import com.petmily.domain.embeded_type.AccountNumber;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import com.petmily.domain.enum_type.BankType;
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
    private Long animalId;
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
    private String backer;

    @NotNull
    private AccountNumber accountNumber;

    @NotNull
    private Integer amount;
}
