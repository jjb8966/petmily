package com.petmily.domain.dto.application;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.embeded_type.AccountNumber;
import com.petmily.domain.enum_type.BankType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ModifyDonationForm {

    private BankType bankType;
    private String backer;
    private AccountNumber accountNumber;
    private Integer amount;
    private AbandonedAnimal abandonedAnimal;

    public ModifyDonationForm(DonationDetailForm form) {
        this.bankType = form.getBankType();
        this.backer = form.getBacker();
        this.accountNumber = form.getAccountNumber();
        this.amount = form.getAmount();
    }
}
