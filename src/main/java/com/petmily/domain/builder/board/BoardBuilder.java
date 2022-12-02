package com.petmily.domain.builder.board;

import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BoardBuilder {

    protected Member member;
    protected List<Reply> replies = new ArrayList<>();
    protected List<Picture> pictures = new ArrayList<>();
    protected BoardType boardType;
    protected String title;
    protected String content;
    protected Boolean shownAll = true;

    public BoardBuilder(Member member, BoardType boardType) {
        this.member = member;
        this.boardType = boardType;
    }

    public Board build() {
        Board board = new Board(this);

        // 연관관계 최신화
        member.getBoards().add(board);

        return board;
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

}
