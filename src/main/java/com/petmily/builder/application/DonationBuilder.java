package com.petmily.builder.application;

import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Member;
import com.petmily.domain.application.Donation;
import com.petmily.enum_type.ApplicationStatus;
import lombok.Getter;

@Getter
public class DonationBuilder {

    private Member member;
    private AbandonedAnimal abandonedAnimal;
    private ApplicationStatus applicationStatus = ApplicationStatus.WAIT;
    private Integer amount;

    public DonationBuilder(Member member, AbandonedAnimal abandonedAnimal) {
        this.member = member;
        this.abandonedAnimal = abandonedAnimal;
    }

    public Donation build() {
        Donation donation = new Donation(this);

        // 연관관계 최신화
        member.getApplies().add(donation);
        abandonedAnimal.getApplications().add(donation);

        return donation;
    }

    public DonationBuilder setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
        return this;
    }

    public DonationBuilder setAmount(Integer amount) {
        this.amount = amount;
        return this;
    }
}
