package com.petmily.domain.dto.application;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ModifyDonationApiForm {

    @NotBlank
    String bankType;

    @NotBlank
    String accountNumber;

    @NotNull
    Integer amount;

    @NotNull
    Long animalId;

}
