package com.petmily.domain.core.board;

import com.petmily.domain.builder.board.WatchBoardBuilder;
import com.petmily.domain.enum_type.AnimalSpecies;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WatchBoard extends Board {

    private LocalDateTime watchTime;
    private AnimalSpecies species;

    public WatchBoard(WatchBoardBuilder builder) {
        super(builder);
        this.watchTime = builder.getWatchTime();
        this.species = builder.getSpecies();
    }
}
