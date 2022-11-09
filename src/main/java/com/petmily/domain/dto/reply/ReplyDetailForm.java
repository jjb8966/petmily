package com.petmily.domain.dto.reply;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class ReplyDetailForm {

    private Long id;
    private Long memberId;
    private LocalDateTime lastModifiedDate;
    private String writerName;
    private String content;
}
