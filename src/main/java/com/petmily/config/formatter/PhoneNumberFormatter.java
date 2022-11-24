package com.petmily.config.formatter;

import com.petmily.domain.core.embeded_type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class PhoneNumberFormatter implements Formatter<PhoneNumber> {

    public static final String START_NUMBER = "010";
    public static final int PHONE_NUMBER_LENGTH = 4;

    private final MessageSource ms;

    @Override
    public PhoneNumber parse(String text, Locale locale) throws ParseException {
        String[] phoneNumber = text.split("-");

        if (phoneNumber.length != 3) {
            throw new ParseException(getMessage("exception.parse.phoneNumber"), 0);
        }

        String startNumber = phoneNumber[0];
        String middleNumber = phoneNumber[1];
        String endNumber = phoneNumber[2];

        if (!startWith010(startNumber) || lengthCheck(middleNumber, endNumber)) {
            throw new ParseException(getMessage("exception.parse.phoneNumber"), 0);
        }

        return new PhoneNumber(startNumber, middleNumber, endNumber);
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

    @Override
    public String print(PhoneNumber object, Locale locale) {
        return object.fullNumber();
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}
