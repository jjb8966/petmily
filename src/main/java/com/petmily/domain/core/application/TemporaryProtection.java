package com.petmily.domain.core.application;

import com.petmily.domain.builder.application.TemporaryProtectionBuilder;
import com.petmily.domain.core.enum_type.LocationType;
import com.petmily.domain.dto.application.ChangeTempProtectionForm;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryProtection extends Application {

    private LocationType location;
    private String job;
    private Boolean married;
    private Integer period;

    public TemporaryProtection(TemporaryProtectionBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.location = builder.getLocation();
        this.job = builder.getJob();
        this.married = builder.isMarried();
        this.period = builder.getPeriod();
    }

    @Override
    public String toString() {
        return "TemporaryProtection{" +
                "address='" + location + '\'' +
                ", job='" + job + '\'' +
                ", married=" + married +
                ", period=" + period +
                ", member=" + member.getName() +
                ", abandonedAnimal=" + abandonedAnimal.getName() +
                ", applicationStatus=" + applicationStatus +
                '}';
    }

    public void changeInfo(ChangeTempProtectionForm tempProtectionDto) {
        this.period = tempProtectionDto.getPeriod();
    }
}
