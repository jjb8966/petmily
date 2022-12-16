package com.petmily.domain.enum_type;

import lombok.Getter;

@Getter
public enum FindWatchBoardStatus {
    LOST("실종"), MATCH("매칭");

    private String description;

    FindWatchBoardStatus(String description) {
        this.description = description;
    }
}
