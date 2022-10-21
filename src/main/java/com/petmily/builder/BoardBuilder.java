package com.petmily.builder;

import com.petmily.domain.Board;
import com.petmily.domain.Member;
import com.petmily.domain.Reply;
import com.petmily.embedded_type.Picture;
import com.petmily.enum_type.BoardType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardBuilder {

    private Member member;
    private List<Reply> replies = new ArrayList<>();
    private List<Picture> pictures = new ArrayList<>();
    private BoardType boardType;
    private String title;
    private String content;
    private boolean shownAll = true;
    private LocalDateTime writeTime;

    public BoardBuilder(Member member, BoardType boardType) {
        this.member = member;
        this.boardType = boardType;
    }

    public Board build() {
        return new Board(this);
    }

    public BoardBuilder setReplies(List<Reply> replies) {
        this.replies = replies;
        return this;
    }

    public BoardBuilder setPictures(List<Picture> pictures) {
        this.pictures = pictures;
        return this;
    }

    public BoardBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public BoardBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public BoardBuilder setShownAll(boolean shownAll) {
        this.shownAll = shownAll;
        return this;
    }

    public BoardBuilder setWriteTime(LocalDateTime writeTime) {
        this.writeTime = writeTime;
        return this;
    }
}
