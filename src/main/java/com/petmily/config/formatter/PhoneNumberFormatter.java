package com.petmily.config.formatter;

import com.petmily.domain.embeded_type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class PhoneNumberFormatter implements Formatter<PhoneNumber> {

    private final MessageSource ms;

    @Override
    public PhoneNumber parse(String text, Locale locale) throws ParseException {
        String[] expression = text.split("-");
        PhoneNumber phoneNumber = null;

        if (expression.length != 3) {
            throw new ParseException(getMessage("exception.parse.phoneNumber"), 0);
        }

        String startNumber = expression[0];
        String middleNumber = expression[1];
        String endNumber = expression[2];

        try {
            phoneNumber = new PhoneNumber(startNumber, middleNumber, endNumber);
        } catch (IllegalArgumentException e) {
            log.info("phone number error = {}", text);
        }

        return phoneNumber;
    }

    @Override
    public String print(PhoneNumber object, Locale locale) {
        return object.fullNumber();
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}
