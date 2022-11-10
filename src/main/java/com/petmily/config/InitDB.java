package com.petmily.config;

import com.petmily.domain.builder.*;
import com.petmily.domain.core.*;
import com.petmily.domain.core.enum_type.AnimalSpecies;
import com.petmily.domain.core.enum_type.BoardType;
import com.petmily.domain.core.enum_type.MemberGrade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.initMember();
        initService.initAbandonedAnimal();
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
                        .setPhone("010-" + i)
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
            Member member = new MemberBuilder("wm", "123").setName("write member").build();

            for (int i = 1; i <= 50; i++) {
                Board board = new BoardBuilder(member, getBoardType(i))
                        .setTitle("board" + i)
                        .setContent("content" + i)
                        .setShownAll(true)
                        .setPictures(new ArrayList<>())
                        .build();

                Reply reply = new ReplyBuilder(member, board)
                        .setContent("reply" + i)
                        .build();

                Picture picture = new PictureBuilder()
                        .setFileStoreName("dog" + (i % 5 + 1) + ".jpeg")
                        .setBoard(board)
                        .build();
            }

            em.persist(member);
        }

        private BoardType getBoardType(int i) {
            if (i % 3 == 0) {
                return BoardType.FREE;
            }

            if (i % 3 == 1) {
                return BoardType.INQUIRY;
            }

            return BoardType.ADOPT_REVIEW;
        }
    }
}
