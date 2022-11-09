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

    private Long id;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private Member member;
    private List<ReplyDetailForm> replies;
    private List<BoardPictureForm> pictures;
    private String title;
    private String content;
    private Boolean shownAll;
}
