package com.petmily.domain.application;

import com.petmily.builder.application.DonationBuilder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Donation extends Application {

    private Integer amount;

    public Donation(DonationBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.amount = builder.getAmount();
    }
}
