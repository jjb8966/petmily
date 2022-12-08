package com.petmily.domain.core.board;

import com.petmily.domain.builder.board.FindWatchBoardBuilder;
import com.petmily.domain.dto.board.ModifyBoardForm;
import com.petmily.domain.enum_type.AnimalSpecies;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindWatchBoard extends Board {

    private LocalDateTime lostOrWatchTime;
    private AnimalSpecies species;
    private String animalName;
    private String animalKind;
    private Integer animalAge;
    private Float animalWeight;

    public FindWatchBoard(FindWatchBoardBuilder builder) {
        super(builder);
        this.lostOrWatchTime = builder.getLostOrWatchTime();
        this.species = builder.getSpecies();
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
}
