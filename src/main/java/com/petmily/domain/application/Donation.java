package com.petmily.domain.application;

import com.petmily.builder.application.DonationBuilder;
import com.petmily.dto.application.ChangeDonationDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Donation extends Application {

    private Integer amount;

    public Donation(DonationBuilder builder) {
        this.member = builder.getMember();
        this.abandonedAnimal = builder.getAbandonedAnimal();
        this.applicationStatus = builder.getApplicationStatus();
        this.amount = builder.getAmount();
    }

    @Override
    public String toString() {
        return "Donation{" +
                "amount=" + amount +
                ", member=" + member +
                ", abandonedAnimal=" + abandonedAnimal +
                ", applicationStatus=" + applicationStatus +
                '}';
    }

    public void changeInfo(ChangeDonationDto donationDto) {
        this.amount = donationDto.getAmount();
    }
}
