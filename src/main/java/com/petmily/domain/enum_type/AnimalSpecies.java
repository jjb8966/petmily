package com.petmily.domain.enum_type;

import lombok.Getter;

@Getter
public enum AnimalSpecies {
    DOG("강아지"), CAT("고양이"), ETC("기타");

    private String description;

    AnimalSpecies(String description) {
        this.description = description;
    }

}
