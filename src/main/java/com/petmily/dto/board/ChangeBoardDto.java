package com.petmily.dto.board;

import com.petmily.embedded_type.Picture;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChangeBoardDto {

    private List<Picture> pictures = new ArrayList<>();
    private String title;
    private String content;
    private boolean shownAll;
}
