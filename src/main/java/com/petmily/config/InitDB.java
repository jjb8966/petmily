package com.petmily.config;

import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.BoardBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.builder.application.AdoptBuilder;
import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.builder.application.TemporaryProtectionBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.Picture;
import com.petmily.domain.core.embeded_type.PhoneNumber;
import com.petmily.domain.core.enum_type.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initMember();
        initService.initAbandonedAnimal();
        initService.initApplication();
        initService.initBoard();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void initMember() {
            for (int i = 1; i <= 5; i++) {
                Member member = new MemberBuilder("m" + i, "123")
                        .setName("member" + i)
                        .setBirth(LocalDate.now())
                        .setEmail("email@" + i + ".com")
                        .setPhoneNumber(new PhoneNumber("010", "1111", "2222"))
                        .build();

                em.persist(member);
            }

            Member admin = new MemberBuilder("ADMIN", "123")
                    .setName("관리자")
                    .setMemberGrade(MemberGrade.ADMIN)
                    .build();

            em.persist(admin);
        }

        public void initAbandonedAnimal() {
            for (int i = 1; i <= 50; i++) {
                AbandonedAnimal animal = new AbandonedAnimalBuilder()
                        .setName("멍멍이" + i)
                        .setAge(i)
                        .setSpecies(AnimalSpecies.DOG)
                        .setKind("진돗개")
                        .setWeight(i)
                        .build();

                String imageName = "dog" + (i % 5 + 1) + ".jpeg";

                Picture picture = new PictureBuilder()
                        .setFileStoreName(imageName)
                        .setAbandonedAnimal(animal)
                        .build();

                em.persist(animal);
            }
        }

        public void initBoard() {
            List<Member> allMembers = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            for (Member member : allMembers) {
                for (int i = 0; i < 6; i++) {
                    if (i % 3 == 0) {
                        new BoardBuilder(member, BoardType.FREE)
                                .setShownAll(true)
                                .setTitle("board" + i)
                                .setContent("content" + i)
                                .build();

                        new BoardBuilder(member, BoardType.FREE)
                                .setShownAll(false)
                                .setTitle("board 비공개" + i)
                                .setContent("content" + i)
                                .build();
                    }

                    if (i % 3 == 1) {
                        new BoardBuilder(member, BoardType.INQUIRY)
                                .setShownAll(true)
                                .setTitle("board" + i)
                                .setContent("content" + i)
                                .build();
                    }

                    if (i % 3 == 2) {
                        new BoardBuilder(member, BoardType.ADOPT_REVIEW)
                                .setShownAll(true)
                                .setTitle("board" + i)
                                .setContent("content" + i)
                                .build();
                    }
                }
            }
        }

        public void initApplication() {
            List<Member> allMembers = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            List<AbandonedAnimal> allAnimals = em.createQuery("select a from AbandonedAnimal a", AbandonedAnimal.class)
                    .getResultList();

            for (Member member : allMembers) {
                for (int i = 0; i < 6; i++) {
                    AbandonedAnimal animal = allAnimals.remove(0);

                    if (i % 3 == 0) {
                        new DonationBuilder(member, animal)
                                .setAccountNumber("1234-1234")
                                .setBankType(BankType.KB)
                                .setAmount(10000)
                                .setDonator(member.getName())
                                .build();
                    }

                    if (i % 3 == 1) {
                        new TemporaryProtectionBuilder(member, animal)
                                .setMarried(true)
                                .setJob("student")
                                .setLocation(LocationType.SEOUL)
                                .setPeriod(10)
                                .build();
                    }

                    if (i % 3 == 2) {
                        new AdoptBuilder(member, animal)
                                .setMarried(true)
                                .setJob("student")
                                .setLocation(LocationType.SEOUL)
                                .build();
                    }
                }
            }
        }
    }
}
