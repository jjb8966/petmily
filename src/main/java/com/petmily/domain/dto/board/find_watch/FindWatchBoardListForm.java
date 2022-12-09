package com.petmily.domain.dto.board.find_watch;

import com.petmily.domain.core.Picture;
import com.petmily.domain.dto.board.BoardListForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class FindWatchBoardListForm extends BoardListForm {

    private Picture thumbnail;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lostOrWatchTime;

    private AnimalSpecies species;

    private FindWatchBoardStatus boardStatus;

    @Override
    public String toString() {
        return "FindWatchBoardListForm{" +
                "thumbnail=" + thumbnail +
                ", lostOrWatchTime=" + lostOrWatchTime +
                ", species=" + species +
                ", id=" + id +
                ", memberId=" + memberId +
                ", writerName='" + writerName + '\'' +
                ", title='" + title + '\'' +
                ", createdDate=" + createdDate +
                ", shownAll=" + shownAll +
                ", boardType=" + boardType +
                '}';
    }
}
