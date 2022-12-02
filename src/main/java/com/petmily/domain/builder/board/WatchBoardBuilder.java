package com.petmily.domain.builder.board;

import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.WatchBoard;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class WatchBoardBuilder extends BoardBuilder {

    private LocalDateTime watchTime;
    private AnimalSpecies species;

    public WatchBoardBuilder(Member member, BoardType boardType) {
        super(member, boardType);
    }

    @Override
    public WatchBoard build() {
        WatchBoard watchBoard = new WatchBoard(this);

        member.getBoards().add(watchBoard);

        return watchBoard;
    }

    @Override
    public WatchBoardBuilder setReplies(List<Reply> replies) {
        super.setReplies(replies);
        return this;
    }

    @Override
    public WatchBoardBuilder setPictures(List<Picture> pictures) {
        super.setPictures(pictures);
        return this;
    }

    @Override
    public WatchBoardBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public WatchBoardBuilder setContent(String content) {
        super.setContent(content);
        return this;
    }

    @Override
    public WatchBoardBuilder setShownAll(boolean shownAll) {
        super.setShownAll(shownAll);
        return this;
    }

    public WatchBoardBuilder setWatchTime(LocalDateTime watchTime) {
        this.watchTime = watchTime;
        return this;
    }

    public WatchBoardBuilder setSpecies(AnimalSpecies species) {
        this.species = species;
        return this;
    }
}
