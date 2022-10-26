package com.petmily.domain.application;

import com.petmily.builder.application.TemporaryProtectionBuilder;
import com.petmily.dto.application.ChangeTempProtectionDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@DiscriminatorValue("temp_protection")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TemporaryProtection extends Application {

    private Integer period;

    public TemporaryProtection(TemporaryProtectionBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.period = builder.getPeriod();
    }

    @Override
    public String toString() {
        return "TemporaryProtection{" +
                "period=" + period +
                ", member=" + member +
                ", abandonedAnimal=" + abandonedAnimal +
                ", applicationStatus=" + applicationStatus +
                '}';
    }

    public void changeInfo(ChangeTempProtectionDto tempProtectionDto) {
        this.period = tempProtectionDto.getPeriod();
    }
}
