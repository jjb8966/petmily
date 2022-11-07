package com.petmily.domain.builder;

import com.petmily.domain.core.Board;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Reply;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReplyBuilder {

    private Member member;
    private Board board;
    private String content;
    private LocalDateTime writeTime;

    public ReplyBuilder(Member member, Board board) {
        this.member = member;
        this.board = board;
    }

    public Reply build() {
        Reply reply = new Reply(this);

        member.getReplies().add(reply);
        board.getReplies().add(reply);

        return reply;
    }

    public ReplyBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public ReplyBuilder setWriteTime(LocalDateTime writeTime) {
        this.writeTime = writeTime;
        return this;
    }
}