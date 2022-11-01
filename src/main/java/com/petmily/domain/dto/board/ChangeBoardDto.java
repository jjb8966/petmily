package com.petmily.domain.dto.board;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChangeBoardDto {

    private String title;
    private String content;
    private boolean shownAll;
}
