package com.petmily.domain.builder;

import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.Board;
import com.petmily.domain.core.board.FindBoard;
import com.petmily.domain.core.board.WatchBoard;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
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
    private LocalDateTime findOrWatchTime;
    private AnimalSpecies species;

    public BoardBuilder(Member member, BoardType boardType) {
        this.member = member;
        this.boardType = boardType;
    }

    public Board build() {
        Board board = null;

        if (boardType.equals(BoardType.FIND)) {
            board = new FindBoard(this);
        } else if (boardType.equals(BoardType.WATCH)) {
            board = new WatchBoard(this);
        } else {
            board = new Board(this);
        }

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

    public BoardBuilder setFindOrWatchTime(LocalDateTime findOrWatchTime) {
        this.findOrWatchTime = findOrWatchTime;
        return this;
    }

    public BoardBuilder setSpecies(AnimalSpecies species) {
        this.species = species;
        return this;
    }
}
