package com.petmily.domain.core.application;

import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.dto.application.ModifyDonationForm;
import com.petmily.domain.embeded_type.AccountNumber;
import com.petmily.domain.enum_type.BankType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Donation extends Application {

    @Enumerated(EnumType.STRING)
    private BankType bankType;

    @Embedded
    private AccountNumber accountNumber;

    private String backer;
    private Integer amount;

    public Donation(DonationBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.bankType = builder.getBankType();
        this.backer = builder.getBacker();
        this.accountNumber = builder.getAccountNumber();
        this.amount = builder.getAmount();
    }

    public void changeInfo(ModifyDonationForm form) {
        this.bankType = form.getBankType();
        this.backer = form.getBacker();
        this.accountNumber = form.getAccountNumber();
        this.amount = form.getAmount();
    }

    public void changeInfoByApi(ModifyDonationForm form) {
        this.bankType = form.getBankType();
        this.accountNumber = form.getAccountNumber();
        this.amount = form.getAmount();
        this.abandonedAnimal = form.getAbandonedAnimal();
    }

    @Override
    public String toString() {
        return "Donation{" +
                "bankType=" + bankType +
                ", backer='" + backer + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", member=" + member.getName() +
                ", abandonedAnimal=" + abandonedAnimal.getName() +
                ", applicationStatus=" + applicationStatus +
                '}';
    }
}
