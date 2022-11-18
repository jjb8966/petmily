package com.petmily.domain.core.embeded_type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneNumber {

    private String startNumber;
    private String middleNumber;
    private String endNumber;

    public PhoneNumber(String startNumber, String middleNumber, String endNumber) {
        this.startNumber = startNumber;
        this.middleNumber = middleNumber;
        this.endNumber = endNumber;
    }

    public String fullNumber() {
        return startNumber + "-" + middleNumber + "-" + endNumber;
    }
}
