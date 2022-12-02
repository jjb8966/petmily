package com.petmily.domain.dto.board;

import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class BoardListForm {

    protected Long id;
    protected Long memberId;
    protected String writerName;
    protected String title;
    protected LocalDateTime createdDate;
    protected Boolean shownAll;
    protected BoardType boardType;
}
