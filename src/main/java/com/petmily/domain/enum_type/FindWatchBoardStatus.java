package com.petmily.domain.enum_type;

import lombok.Getter;

@Getter
public enum FindWatchBoardStatus {
    LOST("실종"), MATCH("매칭 게시글 존재"), COMPLETE("매칭 완료");

    private String description;

    FindWatchBoardStatus(String description) {
        this.description = description;
    }
}
