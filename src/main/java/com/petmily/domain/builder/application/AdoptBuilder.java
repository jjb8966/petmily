package com.petmily.domain.builder.application;

import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.enum_type.ApplicationStatus;
import com.petmily.domain.core.enum_type.LocationType;
import lombok.Getter;

@Getter
public class AdoptBuilder {

    private Member member;
    private AbandonedAnimal abandonedAnimal;
    private ApplicationStatus applicationStatus = ApplicationStatus.WAIT;
    private LocationType location;
    private String job;
    private Boolean married;

    public AdoptBuilder(Member member, AbandonedAnimal abandonedAnimal) {
        this.member = member;
        this.abandonedAnimal = abandonedAnimal;
    }

    public Adopt build() {
        Adopt adopt = new Adopt(this);

        // 연관관계 최신화
        member.getApplications().add(adopt);
        abandonedAnimal.getApplications().add(adopt);

        return adopt;
    }

    public AdoptBuilder setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
        return this;
    }

    public AdoptBuilder setLocation(LocationType location) {
        this.location = location;
        return this;
    }

    public AdoptBuilder setJob(String job) {
        this.job = job;
        return this;
    }

    public AdoptBuilder setMarried(Boolean married) {
        this.married = married;
        return this;
    }

}
