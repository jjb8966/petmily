package com.petmily.domain.embeded_type;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Slf4j
public class AccountNumber {

    private Long accountNumber;
    private String accountNumberWithHyphen;

    public AccountNumber(String accountNumberWithHyphen) {
        this.accountNumberWithHyphen = accountNumberWithHyphen;
        this.accountNumber = convertToInteger(accountNumberWithHyphen);
    }

    private Long convertToInteger(String accountNumberWithHyphen) {
        String[] numbers = accountNumberWithHyphen.split("-");
        String temp = "";
        Long result;

        for (String number : numbers) {
            temp += number;
        }

        try {
            result = Long.parseLong(temp);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
