package com.petmily.config.formatter;

import com.petmily.domain.core.embeded_type.Email;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Locale;

@Slf4j
public class EmailFormatter implements Formatter<Email> {

    @Override
    public Email parse(String text, Locale locale) throws ParseException {
        String[] forId = text.split("@");
        String id = forId[0];

        String[] siteAndDomain = forId[1].split("\\.");
        String site = siteAndDomain[0];
        String domain = siteAndDomain[1];

        return new Email(id, site, domain);
    }

    @Override
    public String print(Email object, Locale locale) {
        return object.fullName();
    }
}
