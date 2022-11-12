package com.petmily.domain.core.application;

import com.petmily.domain.builder.application.AdoptBuilder;
import com.petmily.domain.core.enum_type.LocationType;
import com.petmily.domain.dto.application.ChangeAdoptForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Adopt extends Application {

    private LocationType location;
    private String job;
    private Boolean married;

    public Adopt(AdoptBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.location = builder.getLocation();
        this.job = builder.getJob();
        this.married = builder.getMarried();
    }

    @Override
    public String toString() {
        return "Adopt{" +
                "address='" + location + '\'' +
                ", job='" + job + '\'' +
                ", married=" + married +
                ", member=" + member +
                ", abandonedAnimal=" + abandonedAnimal +
                ", applicationStatus=" + applicationStatus +
                '}';
    }

    public void changeInfo(ChangeAdoptForm adoptDto) {
        this.job = adoptDto.getJob();
        this.married = adoptDto.getMarried();
    }
}
