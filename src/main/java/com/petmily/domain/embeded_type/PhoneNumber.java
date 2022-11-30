package com.petmily.domain.embeded_type;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class PhoneNumber {

    public static final String START_NUMBER = "010";
    public static final int PHONE_NUMBER_LENGTH = 4;

    private String startNumber;
    private String middleNumber;
    private String endNumber;

    public PhoneNumber(String startNumber, String middleNumber, String endNumber) {
        validate(startNumber, middleNumber, endNumber);

        this.startNumber = startNumber;
        this.middleNumber = middleNumber;
        this.endNumber = endNumber;
    }

    private void validate(String startNumber, String middleNumber, String endNumber) {
        if (!startWith010(startNumber) || lengthCheck(middleNumber, endNumber)) {
            throw new IllegalArgumentException();
        }
    }

    private static boolean startWith010(String startNumber) {
        return startNumber.equals(START_NUMBER);
    }

    private static boolean lengthCheck(String middleNumber, String endNumber) {
        return isFourDigitNumber(middleNumber) || isFourDigitNumber(endNumber);
    }

    private static boolean isFourDigitNumber(String number) {
        return number.length() != PHONE_NUMBER_LENGTH;
    }

    public String fullNumber() {
        return startNumber + "-" + middleNumber + "-" + endNumber;
    }
}
