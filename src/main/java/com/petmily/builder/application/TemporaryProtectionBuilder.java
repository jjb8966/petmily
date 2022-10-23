package com.petmily.builder.application;

import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Member;
import com.petmily.domain.application.TemporaryProtection;
import com.petmily.enum_type.ApplicationStatus;
import lombok.Getter;

@Getter
public class TemporaryProtectionBuilder {

    private Member member;
    private AbandonedAnimal abandonedAnimal;
    private ApplicationStatus applicationStatus = ApplicationStatus.WAIT;
    private Integer period;

    public TemporaryProtectionBuilder(Member member, AbandonedAnimal abandonedAnimal) {
        this.member = member;
        this.abandonedAnimal = abandonedAnimal;
    }

    public TemporaryProtection build() {
        TemporaryProtection temporaryProtection = new TemporaryProtection(this);

        // 연관관계 최신화
        member.getApplications().add(temporaryProtection);
        abandonedAnimal.getApplications().add(temporaryProtection);

        return temporaryProtection;
    }

    public TemporaryProtectionBuilder setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
        return this;
    }

    public TemporaryProtectionBuilder setPeriod(Integer period) {
        this.period = period;
        return this;
    }
}
