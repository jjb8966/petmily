package com.petmily.domain.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ModifyBoardApiForm {

    String boardType;
    String title;
    String content;
    Boolean shownAll;

}
