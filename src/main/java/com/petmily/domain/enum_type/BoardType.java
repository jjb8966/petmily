package com.petmily.domain.enum_type;

import lombok.Getter;

@Getter
public enum BoardType {
    FREE("자유 게시판"), INQUIRY("문의 게시판"), ADOPT_REVIEW("입양 후기 게시판");

    private String description;

    BoardType(String description) {
        this.description = description;
    }
}
