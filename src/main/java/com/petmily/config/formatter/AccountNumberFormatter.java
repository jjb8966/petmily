package com.petmily.config.formatter;

import com.petmily.domain.embeded_type.AccountNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
@Component
public class AccountNumberFormatter implements Formatter<AccountNumber> {

    private final MessageSource ms;

    @Override
    public AccountNumber parse(String text, Locale locale) {
        AccountNumber accountNumber = null;

        try {
            accountNumber = new AccountNumber(text);
        } catch (IllegalArgumentException e) {
            log.info("account number error = {}", text);
        }

        return accountNumber;
    }

    @Override
    public String print(AccountNumber object, Locale locale) {
        return object.getAccountNumberWithHyphen();
    }

    private String getMessage(String code) {
        return ms.getMessage(code, null, Locale.KOREA);
    }
}
