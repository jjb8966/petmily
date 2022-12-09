package com.petmily.domain.dto.board.find_watch;

import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.FindWatchBoardStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

@Getter
@Setter
@ToString
public class SearchCondition {

    @Nullable
    private AnimalSpecies species;

    @Nullable
    private FindWatchBoardStatus boardStatus;

    @Nullable
    private String keyword;
}
