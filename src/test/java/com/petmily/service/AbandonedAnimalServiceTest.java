package com.petmily.service;

import com.petmily.builder.AbandonedAnimalBuilder;
import com.petmily.builder.MemberBuilder;
import com.petmily.domain.AbandonedAnimal;
import com.petmily.domain.Member;
import com.petmily.domain.application.Adopt;
import com.petmily.domain.application.Application;
import com.petmily.domain.application.Donation;
import com.petmily.dto.abandoned_animal.ChangeAnimalDto;
import com.petmily.dto.application.ApplyAdoptDto;
import com.petmily.dto.application.ApplyDonationDto;
import com.petmily.embedded_type.Picture;
import com.petmily.enum_type.AnimalSpecies;
import com.petmily.enum_type.AnimalStatus;
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
    AbandonedAnimalService animalService;

    @Autowired
    MemberService memberService;

    @Autowired
    ApplicationService applicationService;

    @Test
    @DisplayName("유기동물 등록 및 id를 통한 조회를 할 수 있다.")
    void register_findOne() {
        //given
        AbandonedAnimal abandonedAnimal = new AbandonedAnimalBuilder()
                .setApplies(new ArrayList<Application>())
                .setPictures(new ArrayList<Picture>())
                .setSpecies(AnimalSpecies.CAT)
                .setStatus(AnimalStatus.ADOPTED)
                .setName("name")
                .setKind("kind")
                .setAge(10)
                .setWeight(1F)
                .build();

        //when
        animalService.register(abandonedAnimal);
        AbandonedAnimal findAnimal = animalService.findOne(abandonedAnimal.getId()).orElseThrow();

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
        animalService.register(animalA);
        animalService.register(animalB);
        animalService.register(animalC);
        animalService.register(animalD);

        //when
        List<AbandonedAnimal> allAnimals = animalService.findAll();

        //then
        assertThat(allAnimals.size()).isEqualTo(4);
        assertThat(allAnimals).containsExactly(animalA, animalB, animalC, animalD);
    }

    @Test
    @DisplayName("유기동물의 정보를 변경할 수 있다.")
    void changeAnimalInfo() {
        //given
        AbandonedAnimal animalA = new AbandonedAnimalBuilder().setName("A").build();
        animalService.register(animalA);

        ChangeAnimalDto animalDto = new ChangeAnimalDto();
        animalDto.setName("B");

        //when
        animalService.changeAnimalInfo(animalA.getId(), animalDto);

        //then
        assertThat(animalA.getName()).isEqualTo("B");
    }

    @Test
    @DisplayName("유기동물을 삭제할 수 있다.")
    void deleteAnimal() {
        //given
        AbandonedAnimal animalA = new AbandonedAnimalBuilder().setName("A").build();
        animalService.register(animalA);

        //when
        animalService.deleteAnimal(animalA.getId());

        //then
        boolean isPresent = animalService.findOne(animalA.getId()).isPresent();
        assertThat(isPresent).isFalse();
    }

    @Test
    @DisplayName("유기동물을 삭제하면 유기동물에 대한 모든 지원서가 삭제된다.")
    void deleteAnimal2() {
        //given
        Member member = new MemberBuilder("memberA", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().setName("animalA").build();
        memberService.join(member);
        animalService.register(animal);

        Long donateId = applicationService.donate(member.getId(), animal.getId(), new ApplyDonationDto());
        Long adoptId = applicationService.adopt(member.getId(), animal.getId(), new ApplyAdoptDto());

        //when
        animalService.deleteAnimal(animal.getId());

        //then
        assertThat(animalService.findOne(animal.getId()).isEmpty()).isTrue();
        assertThat(applicationService.findOne(donateId, Donation.class).isEmpty()).isTrue();
        assertThat(applicationService.findOne(adoptId, Adopt.class).isEmpty()).isTrue();
    }
}