package com.petmily.domain.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
public class ModifyBoardForm {

    private List<MultipartFile> pictures;
    private String title;
    private String content;
    private Boolean shownAll;
}
