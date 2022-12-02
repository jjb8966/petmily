package com.petmily.domain.core.board;

import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.enum_type.AnimalSpecies;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindBoard extends Board {

    private LocalDateTime findTime;
    private AnimalSpecies species;

    public FindBoard(BoardBuilder builder) {
        super(builder);
        this.findTime = builder.getFindOrWatchTime();
        this.species = builder.getSpecies();
    }
}
