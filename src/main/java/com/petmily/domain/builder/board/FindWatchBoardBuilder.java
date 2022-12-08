package com.petmily.domain.builder.board;

import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.FindWatchBoard;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FindWatchBoardBuilder extends BoardBuilder {

    private LocalDateTime lostOrWatchTime;
    private AnimalSpecies species;
    private String animalName;
    private String animalKind;
    private Integer animalAge;
    private Float animalWeight;

    public FindWatchBoardBuilder(Member member, BoardType boardType) {
        super(member, boardType);
    }

    @Override
    public FindWatchBoard build() {
        FindWatchBoard findWatchBoard = new FindWatchBoard(this);

        member.getBoards().add(findWatchBoard);

        return findWatchBoard;
    }

    @Override
    public FindWatchBoardBuilder setReplies(List<Reply> replies) {
        super.setReplies(replies);
        return this;
    }

    @Override
    public FindWatchBoardBuilder setPictures(List<Picture> pictures) {
        super.setPictures(pictures);
        return this;
    }

    @Override
    public FindWatchBoardBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public FindWatchBoardBuilder setContent(String content) {
        super.setContent(content);
        return this;
    }

    @Override
    public FindWatchBoardBuilder setShownAll(boolean shownAll) {
        super.setShownAll(shownAll);
        return this;
    }

    public FindWatchBoardBuilder setLostOrWatchTime(LocalDateTime lostOrWatchTime) {
        this.lostOrWatchTime = lostOrWatchTime;
        return this;
    }

    public FindWatchBoardBuilder setSpecies(AnimalSpecies species) {
        this.species = species;
        return this;
    }

    public FindWatchBoardBuilder setAnimalName(String animalName) {
        this.animalName = animalName;
        return this;
    }

    public FindWatchBoardBuilder setAnimalKind(String animalKind) {
        this.animalKind = animalKind;
        return this;
    }

    public FindWatchBoardBuilder setAnimalAge(Integer animalAge) {
        this.animalAge = animalAge;
        return this;
    }

    public FindWatchBoardBuilder setAnimalWeight(Float animalWeight) {
        this.animalWeight = animalWeight;
        return this;
    }
}
