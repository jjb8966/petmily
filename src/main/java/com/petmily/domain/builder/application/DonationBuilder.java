package com.petmily.domain.builder.application;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.embeded_type.AccountNumber;
import com.petmily.domain.core.enum_type.ApplicationStatus;
import com.petmily.domain.core.enum_type.BankType;
import lombok.Getter;

@Getter
public class DonationBuilder {

    private Member member;
    private AbandonedAnimal abandonedAnimal;
    private ApplicationStatus applicationStatus = ApplicationStatus.WAIT;
    private BankType bankType;
    private String donator;
    private AccountNumber accountNumber;
    private Integer amount;

    public DonationBuilder(Member member, AbandonedAnimal abandonedAnimal) {
        this.member = member;
        this.abandonedAnimal = abandonedAnimal;
    }

    public Donation build() {
        Donation donation = new Donation(this);

        // 연관관계 최신화
        member.getApplications().add(donation);
        abandonedAnimal.getApplications().add(donation);

        return donation;
    }

    public DonationBuilder setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
        return this;
    }

    public DonationBuilder setBankType(BankType bankType) {
        this.bankType = bankType;
        return this;
    }

    public DonationBuilder setDonator(String donator) {
        this.donator = donator;
        return this;
    }

    public DonationBuilder setAccountNumber(AccountNumber accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    public DonationBuilder setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }
}
