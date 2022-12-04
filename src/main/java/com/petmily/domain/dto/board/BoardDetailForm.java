package com.petmily.domain.dto.board;

import com.petmily.domain.core.Member;
import com.petmily.domain.dto.picutre.BoardPictureForm;
import com.petmily.domain.dto.reply.ReplyDetailForm;
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
    protected LocalDateTime createdDate;
    protected LocalDateTime lastModifiedDate;
    protected Member member;
    protected List<ReplyDetailForm> replies;
    protected List<BoardPictureForm> pictures;
    protected String title;
    protected String content;
    protected Boolean shownAll;
}
