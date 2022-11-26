package com.petmily.domain.dto.application;

import com.petmily.domain.core.enum_type.BankType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModifyDonationForm {

    private BankType bankType;
    private String donator;
    private String accountNumber;
    private Integer amount;

    public ModifyDonationForm(DonationDetailForm form) {
        this.bankType = form.getBankType();
        this.donator = form.getDonator();
        this.accountNumber = form.getAccountNumber();
        this.amount = form.getAmount();
    }
}
