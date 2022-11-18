package com.petmily.domain.core.enum_type;

import lombok.Getter;

@Getter
public enum MemberGrade {
    NORMAL("일반 회원"), ADMIN("관리자");

    private String description;

    MemberGrade(String description) {
        this.description = description;
    }
}
