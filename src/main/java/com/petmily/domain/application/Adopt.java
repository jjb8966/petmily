package com.petmily.domain.application;

import com.petmily.builder.application.AdoptBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Adopt extends Application {

    private String address;
    private String job;
    private Character married;

    public Adopt(AdoptBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.address = builder.getAddress();
        this.job = builder.getJob();
        this.married = builder.getMarried();
    }

    @Override
    public String toString() {
        return "Adopt{" +
                "address='" + address + '\'' +
                ", job='" + job + '\'' +
                ", married=" + married +
                ", member=" + member +
                ", abandonedAnimal=" + abandonedAnimal +
                ", applicationStatus=" + applicationStatus +
                '}';
    }
}
