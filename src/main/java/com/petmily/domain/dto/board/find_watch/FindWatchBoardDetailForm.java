package com.petmily.domain.dto.board.find_watch;

import com.petmily.domain.dto.board.BoardDetailForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;

@Getter
@Setter
public class FindWatchBoardDetailForm extends BoardDetailForm {

    @Nullable
    private LocalDateTime lostOrWatchTime;

    @Nullable
    private AnimalSpecies species;

    private FindWatchBoardStatus boardStatus;

    @Nullable
    private String animalName;

    @Nullable
    private String animalKind;

    @Nullable
    @NumberFormat(pattern = "#살")
    private Integer animalAge;

    @Nullable
    @NumberFormat(pattern = "#.#kg")
    private Float animalWeight;

    @Override
    public String toString() {
        return "FindWatchBoardDetailForm{" +
                "lostOrWatchTime=" + lostOrWatchTime +
                ", species=" + species +
                ", id=" + id +
                ", createdDate=" + createdDate +
                ", lastModifiedDate=" + lastModifiedDate +
                ", member=" + member +
                ", replies=" + replies +
                ", pictures=" + pictures +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", shownAll=" + shownAll +
                '}';
    }
}
