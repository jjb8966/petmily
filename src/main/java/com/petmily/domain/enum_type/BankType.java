package com.petmily.domain.enum_type;

public enum BankType {
    KB("국민은행"), IBK("IBK 기업은행"), KEB("KEB 하나은행"), SHINHAN("신한은행"),
    WOORI("우리은행"), NH("농협은행"), TOSS("토스"), KAKAO("카카오뱅크");

    String bankName;

    BankType(String bankName) {
        this.bankName = bankName;
    }

    public String getBankName() {
        return bankName;
    }
}
