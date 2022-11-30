package com.petmily.domain.dto.application;

import com.petmily.domain.core.embeded_type.AccountNumber;
import com.petmily.domain.core.enum_type.BankType;
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
    private String donator;
    private AccountNumber accountNumber;
    private Integer amount;

    public ModifyDonationForm(DonationDetailForm form) {
        this.bankType = form.getBankType();
        this.donator = form.getDonator();
        this.accountNumber = form.getAccountNumber();
        this.amount = form.getAmount();
    }
}
