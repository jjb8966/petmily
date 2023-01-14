package com.petmily.domain.enum_type;

import lombok.Getter;

@Getter
public enum AnimalStatus {
    ADOPTED("입양됨"), PROTECTED("보호소에서 보호중"), TEMP_PROTECTED("임시보호중");

    private String description;

    AnimalStatus(String description) {
        this.description = description;
    }

}
