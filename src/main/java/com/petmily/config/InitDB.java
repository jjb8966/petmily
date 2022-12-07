package com.petmily.config;

import com.petmily.domain.builder.AbandonedAnimalBuilder;
import com.petmily.domain.builder.MemberBuilder;
import com.petmily.domain.builder.PictureBuilder;
import com.petmily.domain.builder.application.AdoptBuilder;
import com.petmily.domain.builder.application.DonationBuilder;
import com.petmily.domain.builder.application.TemporaryProtectionBuilder;
import com.petmily.domain.builder.board.BoardBuilder;
import com.petmily.domain.builder.board.FindBoardBuilder;
import com.petmily.domain.builder.board.WatchBoardBuilder;
import com.petmily.domain.core.AbandonedAnimal;
import com.petmily.domain.core.Member;
import com.petmily.domain.core.board.FindBoard;
import com.petmily.domain.core.board.WatchBoard;
import com.petmily.domain.embeded_type.AccountNumber;
import com.petmily.domain.embeded_type.Email;
import com.petmily.domain.embeded_type.PhoneNumber;
import com.petmily.domain.enum_type.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

//@Component
@RequiredArgsConstructor
@Profile("local")
public class InitDB {

    public static final int NUMBER_OF_MEMBER = 10;
    public static final int NUMBER_OF_ANIMAL = 50;

    public static final int FREE_BOARD = 1;
    public static final int INQUIRY_BOARD = 2;
    public static final int ADOPT_REVIEW_BOARD = 3;
    public static final int FIND_BOARD = 4;
    public static final int WATCH_BOARD = 5;

    public static final int DONATION = 1;
    public static final int TEMP_PROTECTION = 2;
    public static final int ADOPT = 3;

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
            for (int index = 1; index <= NUMBER_OF_MEMBER; index++) {
                Member member = new MemberBuilder("m" + index, "123")
                        .setName("member" + index)
                        .setBirth(LocalDate.now())
                        .setEmail(new Email("abc", "naver", "com"))
                        .setPhoneNumber(new PhoneNumber("010", "1111", "2222"))
                        .build();

                em.persist(member);
            }

            Member admin = new MemberBuilder("ADMIN", "123")
                    .setName("관리자")
                    .setMemberGrade(MemberGrade.ADMIN)
                    .setPhoneNumber(new PhoneNumber("010", "0000", "0000"))
                    .build();

            em.persist(admin);
        }

        public void initAbandonedAnimal() {
            for (int index = 1; index <= NUMBER_OF_ANIMAL; index++) {
                AbandonedAnimal animal = new AbandonedAnimalBuilder()
                        .setName("멍멍이" + index)
                        .setAge(index)
                        .setSpecies(AnimalSpecies.DOG)
                        .setKind("진돗개")
                        .setWeight(index)
                        .build();

                String imageName = "dog" + (index % 5 + 1) + ".jpeg";

                new PictureBuilder()
                        .setFileStoreName(imageName)
                        .setAbandonedAnimal(animal)
                        .build();

                em.persist(animal);
            }
        }

        public void initBoard() {
            List<Member> allMembers = em.createQuery("select m from Member m", Member.class)
                    .getResultList();

            int imageCount = 0;

            for (Member member : allMembers) {
                String catImageName = "cat" + (imageCount++ % 5 + 1) + ".jpeg";    //cat1.jpeg ~ cat5.jpeg
                String dogImageName = "dog" + (imageCount++ % 5 + 1) + ".jpeg";    //dog1.jpeg ~ dog5.jpeg

                for (int kindOfBoard = 1; kindOfBoard <= 5; kindOfBoard++) {
                    if (kindOfBoard == FREE_BOARD) {
                        new BoardBuilder(member, BoardType.FREE)
                                .setShownAll(true)
                                .setTitle("board" + kindOfBoard)
                                .setContent("content" + kindOfBoard)
                                .build();

                        new BoardBuilder(member, BoardType.FREE)
                                .setShownAll(false)
                                .setTitle("board 비공개 " + kindOfBoard)
                                .setContent("content" + kindOfBoard)
                                .build();
                    }

                    if (kindOfBoard == INQUIRY_BOARD) {
                        new BoardBuilder(member, BoardType.INQUIRY)
                                .setShownAll(true)
                                .setTitle("board" + kindOfBoard)
                                .setContent("content" + kindOfBoard)
                                .build();
                    }

                    if (kindOfBoard == ADOPT_REVIEW_BOARD) {
                        new BoardBuilder(member, BoardType.ADOPT_REVIEW)
                                .setShownAll(true)
                                .setTitle("board" + imageCount)
                                .setContent("content" + imageCount)
                                .build();
                    }

                    if (kindOfBoard == FIND_BOARD) {
                        FindBoard findBoard = new FindBoardBuilder(member, BoardType.FIND)
                                .setShownAll(true)
                                .setTitle("board" + kindOfBoard)
                                .setContent("content" + kindOfBoard)
                                .setLostTime(LocalDateTime.now())
                                .setSpecies(AnimalSpecies.CAT)
                                .setAnimalName("야옹이")
                                .setAnimalKind("페르시안")
                                .setAnimalAge(imageCount % 10 + 1)
                                .setAnimalWeight((float) (imageCount % 5 + 1))
                                .build();

                        new PictureBuilder()
                                .setFileStoreName(catImageName)
                                .setBoard(findBoard)
                                .build();
                    }

                    if (kindOfBoard == WATCH_BOARD) {
                        WatchBoard watchBoard = new WatchBoardBuilder(member, BoardType.WATCH)
                                .setShownAll(true)
                                .setTitle("board" + imageCount)
                                .setContent("content" + imageCount)
                                .setWatchTime(LocalDateTime.now())
                                .setSpecies(AnimalSpecies.DOG)
                                .build();

                        new PictureBuilder()
                                .setFileStoreName(dogImageName)
                                .setBoard(watchBoard)
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
                for (int kindOfApplication = 1; kindOfApplication <= 3; kindOfApplication++) {
                    AbandonedAnimal animal = allAnimals.remove(0);

                    if (kindOfApplication == DONATION) {
                        new DonationBuilder(member, animal)
                                .setAccountNumber(new AccountNumber("1234-1234-1234"))
                                .setBankType(BankType.KB)
                                .setAmount(10000)
                                .setDonator(member.getName())
                                .build();
                    }

                    if (kindOfApplication == TEMP_PROTECTION) {
                        new TemporaryProtectionBuilder(member, animal)
                                .setMarried(true)
                                .setJob("student")
                                .setLocation(LocationType.SEOUL)
                                .setPeriod(10)
                                .build();
                    }

                    if (kindOfApplication == ADOPT) {
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
