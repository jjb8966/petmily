package com.petmily.builder.application;

import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Member;
import com.petmily.domain.application.Adopt;
import com.petmily.enum_type.ApplicationStatus;
import lombok.Getter;

@Getter
public class AdoptBuilder {

    private Member member;
    private AbandonedAnimal abandonedAnimal;
    private ApplicationStatus applicationStatus = ApplicationStatus.WAIT;
    private String address;
    private String job;
    private Character married;

    public AdoptBuilder(Member member, AbandonedAnimal abandonedAnimal) {
        this.member = member;
        this.abandonedAnimal = abandonedAnimal;
    }

    public Adopt build() {
        Adopt adopt = new Adopt(this);

        // 연관관계 최신화
        member.getApplies().add(adopt);
        abandonedAnimal.getApplications().add(adopt);

        return adopt;
    }

    public AdoptBuilder setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
        return this;
    }

    public AdoptBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public AdoptBuilder setJob(String job) {
        this.job = job;
        return this;
    }

    public AdoptBuilder setMarried(Character married) {
        this.married = married;
        return this;
    }
}
