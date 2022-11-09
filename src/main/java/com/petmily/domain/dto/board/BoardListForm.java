package com.petmily.domain.dto.board;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardListForm {

    private Long id;
    private Long memberId;
    private String writerName;
    private String title;
    private LocalDateTime createdDate;
    private Boolean shownAll;
}
