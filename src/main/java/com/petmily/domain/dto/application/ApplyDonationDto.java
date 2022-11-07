package com.petmily.domain.dto.application;

import com.petmily.domain.core.enum_type.BankType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
@ToString
public class ApplyDonationDto {

    @NotNull
    private BankType bankType;

    @NotBlank
    private String donator;

    @NotBlank
    private String accountNumber;

    @NotNull
    @Min(10_000L)
    private Integer amount;
}
