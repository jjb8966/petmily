package com.petmily.domain.core.application;

import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.core.embeded_type.AccountNumber;
import com.petmily.domain.core.enum_type.BankType;
import com.petmily.domain.dto.application.ModifyDonationForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Donation extends Application {

    @Enumerated(EnumType.STRING)
    private BankType bankType;

    @Embedded
    private AccountNumber accountNumber;

    private String donator;
    private Integer amount;

    public Donation(DonationBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.bankType = builder.getBankType();
        this.donator = builder.getDonator();
        this.accountNumber = builder.getAccountNumber();
        this.amount = builder.getAmount();
    }

    public void changeInfo(ModifyDonationForm form) {
        this.bankType = form.getBankType();
        this.donator = form.getDonator();
        this.accountNumber = form.getAccountNumber();
        this.amount = form.getAmount();
    }

    @Override
    public String toString() {
        return "Donation{" +
                "bankType=" + bankType +
                ", donator='" + donator + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", member=" + member.getName() +
                ", abandonedAnimal=" + abandonedAnimal.getName() +
                ", applicationStatus=" + applicationStatus +
                '}';
    }
}
