package com.petmily.domain.builder.board;

import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.Reply;
import com.petmily.domain.core.board.FindBoard;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.BoardType;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class FindBoardBuilder extends BoardBuilder {

    private LocalDateTime lostTime;
    private AnimalSpecies species;
    private String animalName;
    private String animalKind;
    private Integer animalAge;
    private Float animalWeight;

    public FindBoardBuilder(Member member, BoardType boardType) {
        super(member, boardType);
    }

    @Override
    public FindBoard build() {
        FindBoard findBoard = new FindBoard(this);

        member.getBoards().add(findBoard);

        return findBoard;
    }

    @Override
    public FindBoardBuilder setReplies(List<Reply> replies) {
        super.setReplies(replies);
        return this;
    }

    @Override
    public FindBoardBuilder setPictures(List<Picture> pictures) {
        super.setPictures(pictures);
        return this;
    }

    @Override
    public FindBoardBuilder setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    @Override
    public FindBoardBuilder setContent(String content) {
        super.setContent(content);
        return this;
    }

    @Override
    public FindBoardBuilder setShownAll(boolean shownAll) {
        super.setShownAll(shownAll);
        return this;
    }

    public FindBoardBuilder setLostTime(LocalDateTime lostTime) {
        this.lostTime = lostTime;
        return this;
    }

    public FindBoardBuilder setSpecies(AnimalSpecies species) {
        this.species = species;
        return this;
    }

    public FindBoardBuilder setAnimalName(String animalName) {
        this.animalName = animalName;
        return this;
    }

    public FindBoardBuilder setAnimalKind(String animalKind) {
        this.animalKind = animalKind;
        return this;
    }

    public FindBoardBuilder setAnimalAge(Integer animalAge) {
        this.animalAge = animalAge;
        return this;
    }

    public FindBoardBuilder setAnimalWeight(Float animalWeight) {
        this.animalWeight = animalWeight;
        return this;
    }
}
