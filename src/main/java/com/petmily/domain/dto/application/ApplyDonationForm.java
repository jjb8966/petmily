package com.petmily.domain.dto.application;

import com.petmily.domain.embeded_type.AccountNumber;
import com.petmily.domain.enum_type.BankType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class ApplyDonationForm {

    @NotNull
    private BankType bankType;

    @NotBlank
    private String donator;

    @NotNull
    private AccountNumber accountNumber;

    @NotNull
    @Min(10_000L)
    private Integer amount;
}
