package com.petmily.domain.core.board;

import com.petmily.domain.builder.board.FindWatchBoardBuilder;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.CascadeType;
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

    private LocalDateTime lostOrWatchTime;
    private AnimalSpecies species;
    private FindWatchBoardStatus boardStatus;
    private String animalName;
    private String animalKind;
    private Integer animalAge;
    private Float animalWeight;

    @OneToMany(mappedBy = "targetBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchInfo> matchedBoardInfos = new ArrayList<>();

    @OneToMany(mappedBy = "matchedBoard", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MatchInfo> targetBoardInfos = new ArrayList<>();

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
        targetBoardInfos.add(matchInfo);
    }

    public void addMatchedBoardInfo(MatchInfo matchInfo) {
        matchedBoardInfos.add(matchInfo);
    }

    public List<FindWatchBoard> getMatchBoards() {
        List<FindWatchBoard> result = new ArrayList<>();

        List<FindWatchBoard> matchedBoardList = getMatchedBoards();
        List<FindWatchBoard> targetBoardList = getTargetBoards();

        result.addAll(matchedBoardList);
        result.addAll(targetBoardList);

        return result;
    }

    private List<FindWatchBoard> getTargetBoards() {
        return targetBoardInfos.stream()
                .map(MatchInfo::getTargetBoard)
                .collect(Collectors.toList());
    }

    private List<FindWatchBoard> getMatchedBoards() {
        return matchedBoardInfos.stream()
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
                ", matchedBoardInfos=" + matchedBoardInfos.size() +
                ", targetBoardInfos=" + targetBoardInfos.size() +
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
}
