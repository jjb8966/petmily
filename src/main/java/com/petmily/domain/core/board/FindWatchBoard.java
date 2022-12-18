package com.petmily.domain.core.board;

import com.petmily.domain.builder.board.FindWatchBoardBuilder;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class FindWatchBoard extends Board {

    @OneToMany(mappedBy = "targetBoard", orphanRemoval = true)
    private List<MatchInfo> whenThisBoardEqTargetBoardInfos = new ArrayList<>();

    @OneToMany(mappedBy = "matchedBoard", orphanRemoval = true)
    private List<MatchInfo> whenThisBoardEqMatchedBoardInfos = new ArrayList<>();

    private LocalDateTime lostOrWatchTime;
    private AnimalSpecies species;
    private FindWatchBoardStatus boardStatus;
    private String animalName;
    private String animalKind;
    private Integer animalAge;
    private Float animalWeight;

    public FindWatchBoard(FindWatchBoardBuilder builder) {
        super(builder);
        this.lostOrWatchTime = builder.getLostOrWatchTime();
        this.species = builder.getSpecies();
        this.boardStatus = builder.getBoardStatus();
        this.animalName = builder.getAnimalName();
        this.animalKind = builder.getAnimalKind();
        this.animalAge = builder.getAnimalAge();
        this.animalWeight = builder.getAnimalWeight();
    }

    @Override
    public void changeInfo(ModifyBoardForm form) {
        super.changeInfo(form);
        this.lostOrWatchTime = form.getLostOrWatchTime();
        this.species = form.getSpecies();
        this.animalName = form.getAnimalName();
        this.animalKind = form.getAnimalKind();
        this.animalAge = form.getAnimalAge();
        this.animalWeight = form.getAnimalWeight();
    }

    public void addTargetBoardInfo(MatchInfo matchInfo) {
        whenThisBoardEqMatchedBoardInfos.add(matchInfo);
    }

    public void addMatchedBoardInfo(MatchInfo matchInfo) {
        whenThisBoardEqTargetBoardInfos.add(matchInfo);
    }

    public List<FindWatchBoard> getAllMatchBoards() {
        List<FindWatchBoard> result = new ArrayList<>();

        List<FindWatchBoard> matchedBoardList = getMatchedBoards();
        List<FindWatchBoard> targetBoardList = getTargetBoards();

        result.addAll(matchedBoardList);
        result.addAll(targetBoardList);

        return result;
    }

    private List<FindWatchBoard> getTargetBoards() {
        return whenThisBoardEqMatchedBoardInfos.stream()
                .map(MatchInfo::getTargetBoard)
                .collect(Collectors.toList());
    }

    private List<FindWatchBoard> getMatchedBoards() {
        return whenThisBoardEqTargetBoardInfos.stream()
                .map(MatchInfo::getMatchedBoard)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "FindWatchBoard{" +
                "lostOrWatchTime=" + lostOrWatchTime +
                ", species=" + species +
                ", boardStatus=" + boardStatus +
                ", animalName='" + animalName + '\'' +
                ", animalKind='" + animalKind + '\'' +
                ", animalAge=" + animalAge +
                ", animalWeight=" + animalWeight +
                ", matchedBoardInfos=" + whenThisBoardEqTargetBoardInfos.size() +
                ", targetBoardInfos=" + whenThisBoardEqMatchedBoardInfos.size() +
                ", id=" + id +
                ", member=" + member.getName() +
                ", replies=" + replies.size() +
                ", pictures=" + pictures.size() +
                ", boardType=" + boardType +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", shownAll=" + shownAll +
                '}';
    }


    public List<Long> getAllMatchInfoIds() {
        List<Long> result = new ArrayList<>();

        whenThisBoardEqTargetBoardInfos.forEach(matchInfo -> result.add(matchInfo.getId()));
        whenThisBoardEqMatchedBoardInfos.forEach(matchInfo -> result.add(matchInfo.getId()));

        return result;
    }

    public void deleteMatchInfo(FindWatchBoard findWatchBoard) {
        checkTargetBoardInfo(findWatchBoard);
        checkMatchedBoardInfo(findWatchBoard);
    }

    private void checkTargetBoardInfo(FindWatchBoard findWatchBoard) {
        whenThisBoardEqTargetBoardInfos
                .removeIf(matchInfo -> matchInfo.getMatchedBoard().equals(findWatchBoard));
    }

    private void checkMatchedBoardInfo(FindWatchBoard findWatchBoard) {
        whenThisBoardEqMatchedBoardInfos
                .removeIf(matchInfo -> matchInfo.getTargetBoard().equals(findWatchBoard));
    }
}
