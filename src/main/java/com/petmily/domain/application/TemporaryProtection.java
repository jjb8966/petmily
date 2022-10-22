package com.petmily.domain.application;

import com.petmily.builder.application.TemporaryProtectionBuilder;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
}
