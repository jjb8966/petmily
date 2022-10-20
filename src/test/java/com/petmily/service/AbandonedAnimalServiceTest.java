package com.petmily.service;

import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Application;
import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.dto.abandoned_animal.ChangeAnimalDto;
import com.petmily.domain.embedded_type.Picture;
import com.petmily.domain.enum_type.AnimalSpecies;
import com.petmily.domain.enum_type.AnimalStatus;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class AbandonedAnimalServiceTest {

    @Autowired
    AbandonedAnimalService service;

    @Test
    @DisplayName("유기동물 등록 및 id를 통한 조회를 할 수 있다.")
    void register_findOne() {
        //given
        AbandonedAnimal abandonedAnimal = new AbandonedAnimalBuilder()
                .setApplications(new ArrayList<Application>())
                .setPictures(new ArrayList<Picture>())
                .setSpecies(AnimalSpecies.CAT)
                .setStatus(AnimalStatus.ADOPTED)
                .setName("name")
                .setKind("kind")
                .setAge(10)
                .setWeight(1F)
                .build();

        //when
        service.register(abandonedAnimal);
        AbandonedAnimal findAnimal = service.findOne(abandonedAnimal.getId()).orElseThrow();

        //then
        assertThat(findAnimal.getAge()).isEqualTo(10);
        assertThat(findAnimal).isEqualTo(abandonedAnimal);
    }

    @Test
    @DisplayName("모든 유기동물을 조회할 수 있다.")
    void findAll() {
        //given
        AbandonedAnimal animalA = new AbandonedAnimalBuilder().setName("A").build();
        AbandonedAnimal animalB = new AbandonedAnimalBuilder().setName("B").build();
        AbandonedAnimal animalC = new AbandonedAnimalBuilder().setName("C").build();
        AbandonedAnimal animalD = new AbandonedAnimalBuilder().setName("D").build();
        service.register(animalA);
        service.register(animalB);
        service.register(animalC);
        service.register(animalD);

        //when
        List<AbandonedAnimal> allAnimals = service.findAll();

        //then
        assertThat(allAnimals.size()).isEqualTo(4);
        assertThat(allAnimals).containsExactly(animalA, animalB, animalC, animalD);
    }

    @Test
    @DisplayName("유기동물의 정보를 변경할 수 있다.")
    void changeAnimalInfo() {
        //given
        AbandonedAnimal animalA = new AbandonedAnimalBuilder().setName("A").build();
        service.register(animalA);

        ChangeAnimalDto animalDto = new ChangeAnimalDto();
        animalDto.setName("B");

        //when
        service.changeAnimalInfo(animalA.getId(), animalDto);

        //then
        assertThat(animalA.getName()).isEqualTo("B");
    }

    @Test
    @DisplayName("유기동물을 삭제할 수 있다.")
    void deleteAnimal() {
        //given
        AbandonedAnimal animalA = new AbandonedAnimalBuilder().setName("A").build();
        service.register(animalA);

        //when
        service.deleteAnimal(animalA.getId());

        //then
        boolean isPresent = service.findOne(animalA.getId()).isPresent();
        assertThat(isPresent).isFalse();
    }
}