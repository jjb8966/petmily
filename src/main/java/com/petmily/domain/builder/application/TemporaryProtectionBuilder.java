package com.petmily.domain.builder.application;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.TemporaryProtection;
import com.petmily.domain.core.enum_type.ApplicationStatus;
import com.petmily.domain.core.enum_type.LocationType;
import lombok.Getter;

@Getter
public class TemporaryProtectionBuilder {

    private Member member;
    private AbandonedAnimal abandonedAnimal;
    private ApplicationStatus applicationStatus = ApplicationStatus.WAIT;
    private LocationType location;
    private String job;
    private boolean married;
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

    public TemporaryProtectionBuilder setLocation(LocationType location) {
        this.location =location;
        return this;
    }

    public TemporaryProtectionBuilder setJob(String job) {
        this.job = job;
        return this;
    }

    public TemporaryProtectionBuilder setMarried(boolean married) {
        this.married = married;
        return this;
    }

    public TemporaryProtectionBuilder setPeriod(Integer period) {
        this.period = period;
        return this;
    }
}
