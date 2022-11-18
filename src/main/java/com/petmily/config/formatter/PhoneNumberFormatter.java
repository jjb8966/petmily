package com.petmily.config.formatter;

import com.petmily.domain.core.embeded_type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class PhoneNumberFormatter implements Formatter<PhoneNumber> {

    @Override
    public PhoneNumber parse(String text, Locale locale) throws ParseException {
        String[] phoneNumber = text.split("-");

        if (phoneNumber.length != 3) {
            throw new ParseException("잘못된 전화번호 형식입니다.", 0);
        }

        String startNumber = phoneNumber[0];
        String middleNumber = phoneNumber[1];
        String endNumber = phoneNumber[2];

        if (!startWith010(startNumber) || lengthCheck(middleNumber, endNumber)) {
            throw new ParseException("잘못된 전화번호 형식입니다.", 0);
        }

        return new PhoneNumber(startNumber, middleNumber, endNumber);
    }

    private static boolean startWith010(String startNumber) {
        return startNumber.equals("010");
    }

    private static boolean lengthCheck(String middleNumber, String endNumber) {
        return isFourDigitNumber(middleNumber) || isFourDigitNumber(endNumber);
    }

    private static boolean isFourDigitNumber(String number) {
        return number.length() != 4;
    }

    @Override
    public String print(PhoneNumber object, Locale locale) {
        return object.fullNumber();
    }
}
