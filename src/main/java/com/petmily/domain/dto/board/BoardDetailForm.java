package com.petmily.domain.dto.board;

import com.petmily.domain.dto.picutre.BoardPictureForm;
import com.petmily.domain.dto.reply.ReplyDetailForm;
import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class BoardDetailForm {

    protected Long id;
    protected Long memberId;
    protected LocalDateTime createdDate;
    protected LocalDateTime lastModifiedDate;
    protected String writerName;
    protected BoardType boardType;
    protected List<ReplyDetailForm> replies;
    protected List<BoardPictureForm> pictures;
    protected String title;
    protected String content;
    protected Boolean shownAll;
}
