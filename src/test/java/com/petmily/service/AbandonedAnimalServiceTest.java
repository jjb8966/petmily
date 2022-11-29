package com.petmily.service;

import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.application.AdoptBuilder;
import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.application.Adopt;
import com.petmily.domain.core.application.Application;
import com.petmily.domain.core.application.Donation;
import com.petmily.domain.core.enum_type.AnimalSpecies;
import com.petmily.domain.dto.abandoned_animal.ChangeAnimalForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Slf4j
class AbandonedAnimalServiceTest {

    @Autowired
    EntityManager em;

    @Autowired
    AbandonedAnimalService animalService;

    @Autowired
    MemberService memberService;

    @Autowired
    ApplicationService applicationService;

    @Test
    @DisplayName("유기동물을 등록할 수 있다.")
    void register() {
        //given
        AbandonedAnimal animal = new AbandonedAnimalBuilder()
                .setName("animal")
                .setKind("진돗개")
                .setAge(1)
                .setWeight(3.0F)
                .build();

        animalService.register(animal);

        //when
        AbandonedAnimal findAnimal = em.find(AbandonedAnimal.class, animal.getId());

        //then
        assertThat(findAnimal).isEqualTo(animal);
    }

    @Test
    @DisplayName("유기동물 id로 유기동물을 조회할 수 있다.")
    void findOne() {
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();
        em.persist(animal);

        Optional<AbandonedAnimal> findAnimalOptional = animalService.findOne(animal.getId());

        assertThat(findAnimalOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("모든 유기동물을 조회할 수 있다.")
    void findAll() {
        //given
        AbandonedAnimal animalA = new AbandonedAnimalBuilder().setName("animalA").build();
        AbandonedAnimal animalB = new AbandonedAnimalBuilder().setName("animalB").build();
        AbandonedAnimal animalC = new AbandonedAnimalBuilder().setName("animalC").build();
        em.persist(animalA);
        em.persist(animalB);
        em.persist(animalC);

        //when
        List<AbandonedAnimal> allAnimals = animalService.findAll();

        //then
        assertThat(allAnimals).hasSize(3);
        assertThat(allAnimals).containsExactly(animalA, animalB, animalC);
    }

    @Test
    @DisplayName("모든 유기동물을 페이징 처리하여 조회할 수 있다.")
    void findAll_paging() {
        //given
        AbandonedAnimal animalA = new AbandonedAnimalBuilder().setAge(6).build();
        AbandonedAnimal animalB = new AbandonedAnimalBuilder().setAge(3).build();   // second
        AbandonedAnimal animalC = new AbandonedAnimalBuilder().setAge(5).build();   // third
        AbandonedAnimal animalD = new AbandonedAnimalBuilder().setAge(7).build();
        AbandonedAnimal animalE = new AbandonedAnimalBuilder().setAge(2).build();   // first
        em.persist(animalA);
        em.persist(animalB);
        em.persist(animalC);
        em.persist(animalD);
        em.persist(animalE);

        //when
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by("age").ascending());
        Page<AbandonedAnimal> allAnimals = animalService.findAll(pageRequest);

        //then
        assertThat(allAnimals).hasSize(3);
        assertThat(allAnimals.getContent()).containsExactly(animalE, animalB, animalC);
        assertThat(allAnimals.getTotalPages()).isEqualTo(2);
        assertThat(allAnimals.getNumber()).isEqualTo(0);
        assertThat(allAnimals.isFirst()).isTrue();
        assertThat(allAnimals.hasNext()).isTrue();
        assertThat(allAnimals.hasPrevious()).isFalse();
    }

    @Test
    @DisplayName("유기동물의 정보를 변경할 수 있다.")
    void changeAnimalInfo() {
        //given
        AbandonedAnimal animal = new AbandonedAnimalBuilder()
                .setName("animalA")
                .setSpecies(AnimalSpecies.DOG)
                .setKind("진돗개")
                .setAge(10)
                .setWeight(4.0F)
                .build();

        em.persist(animal);

        //when
        ChangeAnimalForm changeAnimalForm = new ChangeAnimalForm();
        changeAnimalForm.setName("animalB");
        changeAnimalForm.setSpecies(AnimalSpecies.CAT);
        changeAnimalForm.setKind("잡종");
        changeAnimalForm.setAge(3);
        changeAnimalForm.setWeight(1.3F);

        animalService.changeAnimalInfo(animal.getId(), changeAnimalForm);
        AbandonedAnimal findAnimal = em.find(AbandonedAnimal.class, animal.getId());

        //then
        assertThat(findAnimal.getName()).isEqualTo(changeAnimalForm.getName());
        assertThat(findAnimal.getSpecies()).isEqualTo(changeAnimalForm.getSpecies());
        assertThat(findAnimal.getKind()).isEqualTo(changeAnimalForm.getKind());
        assertThat(findAnimal.getAge()).isEqualTo(changeAnimalForm.getAge());
        assertThat(findAnimal.getWeight()).isEqualTo(changeAnimalForm.getWeight());
    }

    @Test
    @DisplayName("유기동물을 삭제할 수 있다.")
    void deleteAnimal() {
        //given
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();
        em.persist(animal);

        //when
        animalService.deleteAnimal(animal.getId());
        AbandonedAnimal findAnimal = em.find(AbandonedAnimal.class, animal.getId());

        //then
        assertThat(findAnimal).isNull();
    }

    @Test
    @DisplayName("유기동물을 삭제하면 유기동물에 대한 모든 지원서가 삭제된다.")
    void deleteAnimal_with_application() {
        //given
        Member member = new MemberBuilder("member", "123").build();
        AbandonedAnimal animal = new AbandonedAnimalBuilder().build();
        Donation donation = new DonationBuilder(member, animal).build();
        Adopt adopt = new AdoptBuilder(member, animal).build();
        em.persist(member);

        //when
        animalService.deleteAnimal(animal.getId());
        Member findMember = em.find(Member.class, member.getId());
        Application findDonationApplication = em.find(Donation.class, donation.getId());
        Application findAdoptApplication = em.find(Adopt.class, adopt.getId());

        //then
        assertThat(findMember).isNotNull();
        assertThat(findDonationApplication).isNull();
        assertThat(findAdoptApplication).isNull();
    }
}